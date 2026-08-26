#!/usr/bin/env python3
"""Composable 関数の行数を検査し、80行(既定)を超える"新規"違反で失敗する。

ベースライン方式: baseline ファイルに記録済みの違反は許容し、
新規に増えた違反のみ exit 1。リファクタリングで減らしたら
--update-baseline で縮小できる(増やしてはいけない)。

使い方:
  python3 verify-composable-size.py <source-root>... [--baseline FILE] [--max-lines N]
  python3 verify-composable-size.py <source-root>... --update-baseline FILE

抑制: 関数のシグネチャ行に `// size:allow <理由>` を付けると除外。
"""
import argparse
import os
import re
import sys

FUN_RE = re.compile(r'^(\s*)(?:private |internal |public |protected )?(?:suspend |inline )?fun (\w+)<')


def func_blocks(src: "list[str]"):
    """(name, start_line, end_line, signature_line) のリストを返す。"""
    out = []
    i = 0
    while i < len(src):
        m = FUN_RE.match(src[i])
        if m:
            depth, opened, j = 0, False, i
            while j < len(src):
                depth += src[j].count('{') - src[j].count('}')
                if '{' in src[j]:
                    opened = True
                if opened and depth <= 0:
                    break
                j += 1
            out.append((m.group(2), i, j, src[i]))
            i = j
        i += 1
    return out


def scan(root: str, max_lines: int):
    violations = []
    for dirpath, _, files in os.walk(root):
        for f in files:
            if not f.endswith('.kt'):
                continue
            path = os.path.join(dirpath, f)
            rel = os.path.relpath(path)
            try:
                lines = open(path, encoding='utf-8').read().splitlines()
            except UnicodeDecodeError:
                continue
            for name, start, end, sig in func_blocks(lines):
                length = end - start + 1
                if length > max_lines and 'size:allow' not in sig:
                    violations.append(f'{rel}:{name}:{length}')
    return violations


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('roots', nargs='+', help='走査対象ディレクトリ')
    ap.add_argument('--max-lines', type=int, default=80)
    ap.add_argument('--baseline')
    ap.add_argument('--update-baseline', action='store_true')
    args = ap.parse_args()

    violations = []
    for root in args.roots:
        violations += scan(root, args.max_lines)

    if args.update_baseline:
        if not args.baseline:
            sys.exit('--update-baseline には --baseline が必要')
        with open(args.baseline, 'w', encoding='utf-8') as f:
            f.write('\n'.join(sorted(violations)) + ('\n' if violations else ''))
        print(f'baseline updated: {len(violations)} entries -> {args.baseline}')
        return

    baseline = set()
    if args.baseline and os.path.exists(args.baseline):
        baseline = set(
            l.strip() for l in open(args.baseline, encoding='utf-8') if l.strip()
        )

    new_violations = [v for v in violations if v not in baseline]
    if new_violations:
        print(f'FAIL: {len(new_violations)} composable(s) exceed {args.max_lines} lines:')
        for v in sorted(new_violations):
            print(f'  {v}')
        print('分解するか、既存違反は baseline に登録済みかを確認してください。')
        sys.exit(1)

    fixed = baseline - set(violations)
    if fixed:
        print(f'NOTE: baseline のうち {len(fixed)} 件は解消済みです (--update-baseline で縮小できます)。')
    print(f'PASS composable size (max {args.max_lines} lines, {len(violations)} baseline)')


if __name__ == '__main__':
    main()

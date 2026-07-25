#!/usr/bin/env bash
set -euo pipefail

# Diagnose Maven Central GPG signing secrets.
# Supports both:
#   - base64-encoded armored private key (recommended for CI)
#   - raw armored private key (single-line or multi-line)
#
# Run locally:
#   export ORG_GRADLE_PROJECT_signingInMemoryKey="$(base64 -w 0 < signing.key)"
#   export ORG_GRADLE_PROJECT_signingInMemoryKeyId="YOUR_KEY_ID"
#   export ORG_GRADLE_PROJECT_signingInMemoryKeyPassword="YOUR_PASSPHRASE"
#   ./scripts/verify-signing.sh

echo "=== Maven Central signing diagnostic ==="

# -----------------------------------------------------------------------------
# Check 1: SIGNING_KEY secret is present
# -----------------------------------------------------------------------------
if [[ -z "${ORG_GRADLE_PROJECT_signingInMemoryKey:-}" ]]; then
    echo "ERROR: ORG_GRADLE_PROJECT_signingInMemoryKey (SIGNING_KEY) is not set."
    exit 1
fi
echo "OK: SIGNING_KEY is set."

# -----------------------------------------------------------------------------
# Check 2: decode the key (base64 or raw armored)
# -----------------------------------------------------------------------------
key_decoded="$(mktemp)"

# Count newlines. If the secret contains newlines, it is likely the raw armored key.
if [[ "$ORG_GRADLE_PROJECT_signingInMemoryKey" == *$'\n'* ]]; then
    echo "INFO: SIGNING_KEY contains newlines; treating it as a raw armored key."
    printf '%s\n' "$ORG_GRADLE_PROJECT_signingInMemoryKey" > "$key_decoded"
else
    # Try base64 decode first.
    if echo -n "$ORG_GRADLE_PROJECT_signingInMemoryKey" | base64 -d > "$key_decoded" 2>/dev/null; then
        echo "OK: SIGNING_KEY is valid base64."
    else
        # If base64 decode fails, treat it as a single-line armored key.
        echo "INFO: SIGNING_KEY is not base64; treating it as a single-line armored key."
        printf '%s\n' "$ORG_GRADLE_PROJECT_signingInMemoryKey" > "$key_decoded"
    fi
fi

# Normalize the key: insert line breaks before/after armor headers if needed.
key_normalized="$(mktemp)"
awk '
    /-----BEGIN PGP PRIVATE KEY BLOCK-----/ { in_block=1 }
    in_block { print }
    /-----END PGP PRIVATE KEY BLOCK-----/ { in_block=0 }
' "$key_decoded" > "$key_normalized" 2>/dev/null || true

# If awk produced nothing, just use the decoded file as-is.
if [[ ! -s "$key_normalized" ]]; then
    cp "$key_decoded" "$key_normalized"
fi

# -----------------------------------------------------------------------------
# Check 3: decoded content looks like a PGP private key
# -----------------------------------------------------------------------------
if ! grep -q "BEGIN PGP PRIVATE KEY" "$key_normalized"; then
    echo "ERROR: SIGNING_KEY does not contain a PGP PRIVATE KEY block."
    echo "       Make sure the secret is the armored private key (not the public key)."
    echo "       Recommended: base64 -w 0 < private.key"
    rm -f "$key_decoded" "$key_normalized"
    exit 1
fi
echo "OK: SIGNING_KEY contains a PGP private key block."

# -----------------------------------------------------------------------------
# Check 4: SIGNING_KEY_ID is present
# -----------------------------------------------------------------------------
if [[ -z "${ORG_GRADLE_PROJECT_signingInMemoryKeyId:-}" ]]; then
    echo "ERROR: ORG_GRADLE_PROJECT_signingInMemoryKeyId (SIGNING_KEY_ID) is not set."
    rm -f "$key_decoded" "$key_normalized"
    exit 1
fi
echo "OK: SIGNING_KEY_ID is set: $ORG_GRADLE_PROJECT_signingInMemoryKeyId"

# -----------------------------------------------------------------------------
# Check 5: SIGNING_KEY_PASSWORD is present
# -----------------------------------------------------------------------------
if [[ -z "${ORG_GRADLE_PROJECT_signingInMemoryKeyPassword:-}" ]]; then
    echo "ERROR: ORG_GRADLE_PROJECT_signingInMemoryKeyPassword (SIGNING_KEY_PASSWORD) is not set."
    rm -f "$key_decoded" "$key_normalized"
    exit 1
fi
echo "OK: SIGNING_KEY_PASSWORD is set."

# -----------------------------------------------------------------------------
# Check 6: gpg can import the key
# -----------------------------------------------------------------------------
export GNUPGHOME="$(mktemp -d)"
chmod 700 "$GNUPGHOME"
if ! gpg --batch --import "$key_normalized" > /dev/null 2>&1; then
    echo "ERROR: gpg failed to import the private key."
    echo "       The SIGNING_KEY may be corrupted, truncated, or have wrong line breaks."
    rm -rf "$GNUPGHOME" "$key_decoded" "$key_normalized"
    exit 1
fi
echo "OK: gpg can import the private key."

# -----------------------------------------------------------------------------
# Check 7: imported key ID matches SIGNING_KEY_ID
# -----------------------------------------------------------------------------
imported_key_id="$(gpg --list-secret-keys --keyid-format long | awk '/^sec/{print $2}' | cut -d'/' -f2)"
if [[ -z "$imported_key_id" ]]; then
    echo "ERROR: Could not determine the key ID from the imported key."
    rm -rf "$GNUPGHOME" "$key_decoded" "$key_normalized"
    exit 1
fi

# Accept either the full 16-char key ID or the short 8-char ID.
if [[ "$imported_key_id" != "$ORG_GRADLE_PROJECT_signingInMemoryKeyId"* ]] && \
   [[ "$ORG_GRADLE_PROJECT_signingInMemoryKeyId" != "$imported_key_id"* ]]; then
    echo "ERROR: SIGNING_KEY_ID does not match the imported key."
    echo "       SIGNING_KEY_ID: $ORG_GRADLE_PROJECT_signingInMemoryKeyId"
    echo "       Imported key:   $imported_key_id"
    rm -rf "$GNUPGHOME" "$key_decoded" "$key_normalized"
    exit 1
fi
echo "OK: SIGNING_KEY_ID matches the imported key: $imported_key_id"

# -----------------------------------------------------------------------------
# Check 8: passphrase can decrypt the key
# -----------------------------------------------------------------------------
if ! gpg --batch --pinentry-mode loopback \
    --passphrase "$ORG_GRADLE_PROJECT_signingInMemoryKeyPassword" \
    --export-secret-keys "$imported_key_id" > /dev/null 2>&1; then
    echo "ERROR: The passphrase (SIGNING_KEY_PASSWORD) is incorrect."
    echo "       gpg cannot decrypt the private key with the provided password."
    echo "       This is the most likely cause of: PGPException: checksum mismatch"
    rm -rf "$GNUPGHOME" "$key_decoded" "$key_normalized"
    exit 1
fi
echo "OK: Passphrase successfully decrypts the private key."

# -----------------------------------------------------------------------------
# Cleanup
# -----------------------------------------------------------------------------
rm -rf "$GNUPGHOME" "$key_decoded" "$key_normalized"

echo ""
echo "=== All signing checks passed ==="
echo "If the CI build still fails, verify the Maven Central credentials (MAVEN_CENTRAL_USERNAME / MAVEN_CENTRAL_PASSWORD)."

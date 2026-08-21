# Release evidence index

`build/reports/release/` is the machine-readable evidence bundle for a
candidate release. The producer is Gradle (`generateSbom`); the independent
consumer is `scripts/verify-release-evidence.py`.

The verifier checks all of the following before an artifact is indexed:

- the CycloneDX 1.5 SBOM is present and has a non-empty component list;
- every dependency with a binary hash has exactly one matching SHA-256 line in
  `checksums.txt` (including Maven coordinates, so duplicate cache file names
  cannot be confused);
- `third-party-notices.txt` carries the source revision and covers every
  dependency represented by the SBOM;
- `license-check.txt` has a component count matching the SBOM and explicitly
  says either `REVIEW_REQUIRED` or `VERIFIED`.

The optional `--write-manifest` flag writes
`build/reports/release/evidence-manifest.json`, containing the source revision,
license status, byte sizes, and SHA-256 digests of the four reports. The
manifest deliberately excludes itself to avoid a self-referential checksum.

```text
./gradlew generateSbom --no-daemon
python3 scripts/verify-release-evidence.py --write-manifest
```

Normal CI may retain `REVIEW_REQUIRED` while legal review is pending. The
release workflow separately runs `scripts/verify-sbom.py --require-clean`; a
release must not describe the SBOM as verified while any dependency remains in
the review set. A passing local verifier is therefore evidence of report
integrity, not evidence that hosted CI, device accessibility, iOS runtime, or
legal approval has occurred.

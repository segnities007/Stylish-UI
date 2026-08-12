# Security Policy

## Supported Versions

Stylish UI is a pre-1.0 library. Security fixes are released with the next
minor or patch release; we do not backport fixes to older minor versions
before 1.0.0.

| Version | Supported |
|---------|-----------|
| Latest release | ✅ |
| Older pre-1.0 releases | ❌ |

## Reporting a Vulnerability

Please report security vulnerabilities through **GitHub private
vulnerability reporting** instead of a public issue:

1. Open the repository's **Security** tab.
2. Select **Report a vulnerability** (green button).
3. Describe the vulnerability, the affected version(s), and — if possible —
   a minimal reproduction.

We will acknowledge your report within 3 business days and work with you to
understand and fix the issue. You will be credited in the release notes
unless you prefer to remain anonymous.

Please do **not** file a public issue or PR for a security vulnerability.

## Security Considerations for This Library

- Stylish UI is a UI component library and does not handle credentials,
  network requests, or persistence on its own. Treat the library as
  untrusted-renderer input when reflecting user data into components.
- Components never log data that flows through their parameters.
- If you depend on Stylish UI through a binary repository, verify the
  checksums published with each release.

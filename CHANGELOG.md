# Changelog

## [0.13.0](https://github.com/segnities007/Stylish-UI/compare/v0.12.0...v0.13.0) (2026-08-26)


### ⚠ BREAKING CHANGES

* removes StylishGlassSurface, StylishJapaneseGlass, StylishFrostedGlassSurface, StylishGlassState/stylishGlassSource, and glass/backdrop/glassState parameters from Fab/Header/ModernScreen/ BottomAppBar/NavigationBar/ShortNavigationBar/SearchBar. Glass catalog demos removed.
* StylishHeader no longer self-insets the status bar
* rename StylishScreenScaffold to StylishScaffold

### Features

* **api:** name floating top bar, bottom bar, and fab explicitly ([989ce01](https://github.com/segnities007/Stylish-UI/commit/989ce010bafd0674ac9c319a8b272073666cafc8))
* **atoms:** add StylishGlassSurface — portable Liquid-Glass-look surface ([7ed036d](https://github.com/segnities007/Stylish-UI/commit/7ed036dca4eb4b58d39c0f98d83305ac130c126f))
* **atoms:** glass variants (Regular/Clear), theme-aware tint/border, interactive sheen — per Apple Liquid Glass guidance ([0247f84](https://github.com/segnities007/Stylish-UI/commit/0247f8485d932fdf466d58c16d8ef823366d9457))
* **atoms:** Japanese glass presets (Sumi/Ai/Matcha/Kinpaku/Sakura/Kasumi/Mizu/Beni) + overload and showcase previews ([fe94b2b](https://github.com/segnities007/Stylish-UI/commit/fe94b2bf83d177846e63680b407ae7159d2a03ca))
* **atoms:** StylishFrostedGlassSurface (ground glass) + frosted backdrop support on Fab/Header ([e0c4580](https://github.com/segnities007/Stylish-UI/commit/e0c45800e65f9914edae7eb418af99da866f716c))
* **catalog:** glass variants and japanese presets demos ([056e0cf](https://github.com/segnities007/Stylish-UI/commit/056e0cf3fbcb55510635ab97f9d28a3eaf6acfc9))
* **fab:** default to M3 FAB spec colors (primaryContainer/onPrimaryContainer) ([9709847](https://github.com/segnities007/Stylish-UI/commit/9709847c8e13d3f409399f3567cd82a63a15c033))
* **floating:** unify opacity for fab dialogs and bottom sheets ([d9c48cf](https://github.com/segnities007/Stylish-UI/commit/d9c48cf19acec4b4aeda00d6b59423e9ed2816e7))
* **glass:** ground-glass mode for floating bars (BottomAppBar/NavigationBar/ShortNavigationBar/SearchBar) ([0745458](https://github.com/segnities007/Stylish-UI/commit/07454585144c11ade4b9648f9a20428f7a9fa06b))
* **glass:** live ground glass via GraphicsLayer record/replay ([53cebd5](https://github.com/segnities007/Stylish-UI/commit/53cebd5aa5c977c698d6900496a6acbb862fba0e))
* **glass:** theme-aware header tint/border + borderColor param (light-mode visibility) ([f6b00b4](https://github.com/segnities007/Stylish-UI/commit/f6b00b452cbe3acc2a398dff6cf5a6939fc4c511))
* **header:** apply primaryContainer accent in light floating header ([67e79c7](https://github.com/segnities007/Stylish-UI/commit/67e79c777de3a8c26cb37d72ea1ed6b2b4a0cafc))
* **header:** default floating translucency (surfaceContainerHigh 90%) matching PagerIndicator ([168c3d6](https://github.com/segnities007/Stylish-UI/commit/168c3d6470680b652105760b659a2fa5e5e00a49))
* **patterns:** add StylishModernScreen — scroll behavior built into the scaffold ([b4ae75d](https://github.com/segnities007/Stylish-UI/commit/b4ae75de06bdfcb3a12d2e76e295b503f7ed26f2))
* **patterns:** add StylishScreenScaffold for pinned-header pages ([184708c](https://github.com/segnities007/Stylish-UI/commit/184708cd7df4e83c32fc367b6a700cbca2cb7a31))
* **patterns:** automatic scroll edge effect in StylishModernScreen ([6b646d3](https://github.com/segnities007/Stylish-UI/commit/6b646d3e8ed1d3363efc7a6a5959d00628031066))
* **patterns:** distinct surfaceContainer status-bar band with rounded lip ([f7f56c5](https://github.com/segnities007/Stylish-UI/commit/f7f56c51bfce68bf2b40bcf999f14f9359e26b1d))
* **patterns:** expose statusBarScrimColor on StylishScreenScaffold ([0507b5b](https://github.com/segnities007/Stylish-UI/commit/0507b5b2902ebac1c8d2d6b99fa7723e827b5499))
* **patterns:** fade scrolled content into the container across the nav-bar zone ([4830d46](https://github.com/segnities007/Stylish-UI/commit/4830d46133aa0740b06129fd03f8094666ceef9c))
* **patterns:** glass mode for StylishHeader — translucent pill with sheen and highlight border ([26101ff](https://github.com/segnities007/Stylish-UI/commit/26101ff38e1b2cdc981887e5e6965eed5d6e1f14))
* **patterns:** optional hide-on-scroll for StylishScreenScaffold ([c066545](https://github.com/segnities007/Stylish-UI/commit/c066545ca2d0d7b8610ff6007479bfb70a7a55cf))
* **patterns:** soft scroll edge effect behind the ModernScreen header ([7da0333](https://github.com/segnities007/Stylish-UI/commit/7da0333bd6481fb109488b8719d1c1d7702e89b4))
* remove glass UI system entirely ([3b606e3](https://github.com/segnities007/Stylish-UI/commit/3b606e3fc27572f29654bde6cb6e6b1a913e48b3))
* **scroll:** full slide-in/out on scroll direction detection (no proportional tracking) ([0e1546f](https://github.com/segnities007/Stylish-UI/commit/0e1546f035d36ddff587eb4eefaad2cf902500fa))


### Bug Fixes

* **button:** align icon button opacity with connected floating controls ([da40bc9](https://github.com/segnities007/Stylish-UI/commit/da40bc9badb1a9a348c88e8e3604529215c555ce))
* **glass:** frosted surface requires explicit size in modifier (height-0 header fix) ([c6e5180](https://github.com/segnities007/Stylish-UI/commit/c6e5180c5cb7cd5780732ec23416ddda9539e4ae))
* **glass:** remove draw-phase snapshot writes; explicit invalidateDraw ([e236ffd](https://github.com/segnities007/Stylish-UI/commit/e236ffd18b17de993dc574ee472af0c0f1450509))
* **header:** center title across full bar width (overlay layout) ([27c8f8d](https://github.com/segnities007/Stylish-UI/commit/27c8f8dd335a6de63120b18cd172f9830bccb484))
* move glass floating-family preview to patterns (atoms must not import patterns) ([dafe141](https://github.com/segnities007/Stylish-UI/commit/dafe141e728a47dc8e3275b782fa13b5936094aa))
* **patterns:** always show floating layer when the list is at the top ([740ee50](https://github.com/segnities007/Stylish-UI/commit/740ee502a5edc5aa2a6c6b2c2d1a7956f2b2eae1))
* **patterns:** color only the status-bar strip, not the whole header block ([0dc0319](https://github.com/segnities007/Stylish-UI/commit/0dc0319c37999f9b24f4b294030ce2aef0c3c51e))
* **patterns:** consume the status-bar inset in StylishModernScreen's header ([f73c3aa](https://github.com/segnities007/Stylish-UI/commit/f73c3aa5dd556e14472b16d82b84ffd1c4803283))
* **patterns:** don't add status-bar spacer on top of self-insetting headers ([3053179](https://github.com/segnities007/Stylish-UI/commit/3053179335af663a3d55a46417e20e61bc14902b))
* **patterns:** drop the header backdrop band — header floats clean again ([b79eca0](https://github.com/segnities007/Stylish-UI/commit/b79eca0d3b1b3738a1817ef3722cf151661c0288))
* **patterns:** import statusBars for the inset consumption ([ca5f77a](https://github.com/segnities007/Stylish-UI/commit/ca5f77a29b8dd394b61162e3879c78e39a7a6f5d))
* **patterns:** keep header height stable while the header slides away ([a996457](https://github.com/segnities007/Stylish-UI/commit/a996457b57e844a2c0172e8ba2749dbf48d8b418))
* **patterns:** make the status-bar strip semi-transparent ([bd5933b](https://github.com/segnities007/Stylish-UI/commit/bd5933b89cf71967d6be96cbee30df844d3d08d8))
* **patterns:** measure header height instead of hard-coded top spacing ([9cad26d](https://github.com/segnities007/Stylish-UI/commit/9cad26d8b077e93bcfdfab234d82d941daafe8a4))
* **patterns:** missing nestedScroll import ([43b080d](https://github.com/segnities007/Stylish-UI/commit/43b080d6ab4e6f5d744b90f8660c1889a7abad62))
* **patterns:** restore status-bar clearance in screen scaffold header ([247dc6e](https://github.com/segnities007/Stylish-UI/commit/247dc6e23f375fcf2c1525c02d4e29ac3c258337))
* **patterns:** status-bar scrim defaults to transparent ([a9e4dd7](https://github.com/segnities007/Stylish-UI/commit/a9e4dd7f322b461b57e4b6d01704ad108d7d9cad))
* **patterns:** survive page disposal — rememberSaveable for header height ([8efdc92](https://github.com/segnities007/Stylish-UI/commit/8efdc9223ce2c16c122411ee7f8d42e367ed9e59))
* **patterns:** synchronous header measurement + finger-tracking slide in StylishModernScreen ([f832bcf](https://github.com/segnities007/Stylish-UI/commit/f832bcf27b0089d239d2f55931c412296dbdfd21))
* **patterns:** use a translucent dark scrim for the nav-bar zone ([b4ddcf6](https://github.com/segnities007/Stylish-UI/commit/b4ddcf67879b0b7e5984b2d0819a0a7ac9a6180f))
* **tokens:** align connectedSpacing with hand-built connected lists (3dp -&gt; 4dp) ([be7bd75](https://github.com/segnities007/Stylish-UI/commit/be7bd75a11028e2877156a72f73a693eb103f5b5))
* **website:** repair desktop app entry (desktopMain source set, window state, material3 dep) ([93dede5](https://github.com/segnities007/Stylish-UI/commit/93dede5054d3b3f158fb21077ca3908b0381aa6c))


### Reverts

* **patterns:** drop bottom nav-bar scrim (not needed) ([02e92e6](https://github.com/segnities007/Stylish-UI/commit/02e92e65f06b75bb7453b954c2ee6be46861e7e4))


### Code Refactoring

* rename StylishScreenScaffold to StylishScaffold ([c429460](https://github.com/segnities007/Stylish-UI/commit/c4294603aaad5a32a14c999d4eb024dd833323d6))
* StylishHeader no longer self-insets the status bar ([49973a8](https://github.com/segnities007/Stylish-UI/commit/49973a80ebbfc2617f46546bd760ab5bd004af84))

## [0.12.0](https://github.com/segnities007/Stylish-UI/compare/v0.11.3...v0.12.0) (2026-08-24)


### ⚠ BREAKING CHANGES

* remove StylishConnectedListItem family in favor of Connect… ([#53](https://github.com/segnities007/Stylish-UI/issues/53))

### Bug Fixes

* **components:** always clip card press indication to the item shape ([712d6af](https://github.com/segnities007/Stylish-UI/commit/712d6af1ce5ce13bc2d50c060db5f86735e30720))
* **components:** clip card indication and state layer to exact per-corner shapes ([46863c8](https://github.com/segnities007/Stylish-UI/commit/46863c84765427ec2b41feee1391c0c9aa3b3464))
* **components:** default bottom sheet scrim to M3 32% alpha ([42b69be](https://github.com/segnities007/Stylish-UI/commit/42b69be9b6bb1a7266241b4cf06a8600f6a353af))


### Code Refactoring

* remove StylishConnectedListItem family in favor of Connect… ([#53](https://github.com/segnities007/Stylish-UI/issues/53)) ([ca8de5a](https://github.com/segnities007/Stylish-UI/commit/ca8de5ab40c2acc4dc7a570eaaf57af2ab073e7d))

## [0.11.3](https://github.com/segnities007/Stylish-UI/compare/v0.11.2...v0.11.3) (2026-08-22)


### Bug Fixes

* stabilize animated component layout and toasts ([#50](https://github.com/segnities007/Stylish-UI/issues/50)) ([93962c1](https://github.com/segnities007/Stylish-UI/commit/93962c1da061a7861c9c49a4b11ac782bec01a2a))

## [0.11.2](https://github.com/segnities007/Stylish-UI/compare/v0.11.1...v0.11.2) (2026-08-21)


### Bug Fixes

* **ci:** remove deleted design handoff verifier ([#48](https://github.com/segnities007/Stylish-UI/issues/48)) ([91eeb83](https://github.com/segnities007/Stylish-UI/commit/91eeb832f7554ebd6fbb34964797e5785a494e35))

## [0.11.1](https://github.com/segnities007/Stylish-UI/compare/v0.11.0...v0.11.1) (2026-08-21)


### Bug Fixes

* **release:** allow unresolved SBOM licenses in publish gate ([#46](https://github.com/segnities007/Stylish-UI/issues/46)) ([d3f5dd5](https://github.com/segnities007/Stylish-UI/commit/d3f5dd5d1a11068f0966709fa9914c98f4addba9))

## [0.11.0](https://github.com/segnities007/Stylish-UI/compare/v0.10.0...v0.11.0) (2026-08-21)


### Features

* **adoption:** expand catalog coverage and harden release evidence g… ([#44](https://github.com/segnities007/Stylish-UI/issues/44)) ([4148383](https://github.com/segnities007/Stylish-UI/commit/4148383e38c5e0666bf0c03b30850dcb0dc13289))

## [0.10.0](https://github.com/segnities007/Stylish-UI/compare/v0.9.0...v0.10.0) (2026-08-13)


### Features

* **components:** add round-3 web parity components (command palette, splitter, masonry, etc.) ([#39](https://github.com/segnities007/Stylish-UI/issues/39)) ([0cfcd6b](https://github.com/segnities007/Stylish-UI/commit/0cfcd6b105e49ff3a3ad7f6e44494bbf7953ea50))
* **website:** rebuild the official site as an interactive playground ([#35](https://github.com/segnities007/Stylish-UI/issues/35)) ([da28c6f](https://github.com/segnities007/Stylish-UI/commit/da28c6f7e4572eee7d8f20fb2108f4416ee2fd0c))
* **website:** rebuild the official site as an interactive playground ([#38](https://github.com/segnities007/Stylish-UI/issues/38)) ([0e3098b](https://github.com/segnities007/Stylish-UI/commit/0e3098bb20eef2bc0b01cb7eeb6097560dfd921d))

## [0.9.0](https://github.com/segnities007/Stylish-UI/compare/v0.8.0...v0.9.0) (2026-08-13)


### Features

* **components:** add round-2 web parity components (alert, toast, result, popconfirm, etc.) ([#32](https://github.com/segnities007/Stylish-UI/issues/32)) ([9417f2c](https://github.com/segnities007/Stylish-UI/commit/9417f2c0874dbe8dc21184e3766454bbd2f2937a))


### Bug Fixes

* **release:** sync version.properties and use inline release-please marker ([#33](https://github.com/segnities007/Stylish-UI/issues/33)) ([03022ae](https://github.com/segnities007/Stylish-UI/commit/03022aebb71c55835885ac09474ae9807f0be1e4))

## [0.8.0](https://github.com/segnities007/Stylish-UI/compare/v0.7.0...v0.8.0) (2026-08-12)


### Features

* quality hardening ([#30](https://github.com/segnities007/Stylish-UI/issues/30)) ([508b770](https://github.com/segnities007/Stylish-UI/commit/508b770129e7aecce478d10aa82e72ee3a6a92e8))

## [0.7.0](https://github.com/segnities007/Stylish-UI/compare/v0.6.1...v0.7.0) (2026-08-12)


### Features

* library hardening ([#28](https://github.com/segnities007/Stylish-UI/issues/28)) ([91884a3](https://github.com/segnities007/Stylish-UI/commit/91884a361e87732796f05c4540149531c77598bf))

## [0.6.1](https://github.com/segnities007/Stylish-UI/compare/v0.6.0...v0.6.1) (2026-07-29)


### Bug Fixes

* /navigation bar and content mode ([#24](https://github.com/segnities007/Stylish-UI/issues/24)) ([b339938](https://github.com/segnities007/Stylish-UI/commit/b339938b7c7f7814c81d4ccc204f1d41c76e826b))

## [0.6.0](https://github.com/segnities007/Stylish-UI/compare/v0.5.1...v0.6.0) (2026-07-29)


### Features

* **components:** add missing components and refactor token-based spacing ([#22](https://github.com/segnities007/Stylish-UI/issues/22)) ([fdaaa09](https://github.com/segnities007/Stylish-UI/commit/fdaaa0978c2c8d10abe10686f806b3edf11d72d9))

## [0.5.1](https://github.com/segnities007/Stylish-UI/compare/v0.5.0...v0.5.1) (2026-07-28)


### Bug Fixes

* **ci:** pin publish version to release tag and fix Release Please extra-files ([#17](https://github.com/segnities007/Stylish-UI/issues/17)) ([490e382](https://github.com/segnities007/Stylish-UI/commit/490e3823fc8b9ddfeae3834969f5791389e267c9))

## [0.5.0](https://github.com/segnities007/Stylish-UI/compare/v0.4.0...v0.5.0) (2026-07-28)


### Features

* introduce Foundation/Structure/Finish ([#15](https://github.com/segnities007/Stylish-UI/issues/15)) ([00fbc34](https://github.com/segnities007/Stylish-UI/commit/00fbc34e3fefbf55ddb3bd93b50aea5cb73cc702))

## [0.4.0](https://github.com/segnities007/Stylish-UI/compare/v0.3.0...v0.4.0) (2026-07-27)


### Features

* improve component flexibility ([#11](https://github.com/segnities007/Stylish-UI/issues/11)) ([72d4ecb](https://github.com/segnities007/Stylish-UI/commit/72d4ecba3823c2686bdced2b93e8c6b39c7ffce4))
* improve component flexibility ([#9](https://github.com/segnities007/Stylish-UI/issues/9)) ([a3643ff](https://github.com/segnities007/Stylish-UI/commit/a3643ffaf9fc7fb48891eecfb7f933af3f5d6608))

## [0.3.0](https://github.com/segnities007/Stylish-UI/compare/v0.2.1...v0.3.0) (2026-07-25)


### Features

* component flexibility ([#7](https://github.com/segnities007/Stylish-UI/issues/7)) ([95c0ae3](https://github.com/segnities007/Stylish-UI/commit/95c0ae314b5aecb91e3324254384c475270e19b9))

## [0.2.1](https://github.com/segnities007/Stylish-UI/compare/v0.2.0...v0.2.1) (2026-07-25)


### Bug Fixes

* **ci:** eagerly resolve version provider and migrate to KMP structure ([#5](https://github.com/segnities007/Stylish-UI/issues/5)) ([8f58246](https://github.com/segnities007/Stylish-UI/commit/8f58246d6d5ce3bd7d191917760c22bde32d35b5))

## [0.2.0](https://github.com/segnities007/Stylish-UI/compare/v0.1.0...v0.2.0) (2026-07-25)


### Features

* **ci:** add Release Please, PR checks, and contribution guidelines ([3937cbe](https://github.com/segnities007/Stylish-UI/commit/3937cbeb4d5875dc17986e89e32216ab528b3e53))
* **components:** add text overflow, style, dimension, and color customization params ([#2](https://github.com/segnities007/Stylish-UI/issues/2)) ([e0993f9](https://github.com/segnities007/Stylish-UI/commit/e0993f9ee59e0fd7bfe1c0ccced14c451142e3eb))

## 0.1.0 (2026-07-25)

### Features

- Initial release of Stylish UI
- Compose design system components for Android
- Connected geometry utilities
- Theme, tokens, and component layers
- Maven Central publication setup

### Documentation

- Added README with usage examples
- Added Apache License 2.0

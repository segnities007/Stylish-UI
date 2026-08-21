plugins {
    // Migration canary: proves a single downstream import surface can span BOTH
    // extracted artifacts (:foundation + :structure) without depending on the
    // styled root publication. The orchestrator compiles this module as part of
    // the staged Structure/Components split (allowlist in scripts/verify-module-boundaries.py).
    alias(libs.plugins.kotlin.multiplatform)
}

group = "io.github.segnities007"

kotlin {
    explicitApi()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":foundation"))
            implementation(project(":structure"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

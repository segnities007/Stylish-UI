plugins {
    // This sample is intentionally UI-framework free.  Its contracts are consumed
    // by Android, JVM Desktop, Wasm/Web, and Apple hosts without importing Compose.
    alias(libs.plugins.kotlin.multiplatform)
}

group = "io.github.segnities007"

kotlin {
    explicitApi()

    jvm()

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }

        jvmTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

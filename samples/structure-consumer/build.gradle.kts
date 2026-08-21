plugins {
    // This sample is intentionally small: it proves a downstream app can use
    // the physical Structure artifact without depending on the styled root.
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

group = "io.github.segnities007"

kotlin {
    explicitApi()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":structure"))
            // The sample composes @Composable slots from :structure, so the
            // Compose runtime must be on this module's own compile classpath
            // for the applied Compose compiler plugin.
            implementation(libs.compose.runtime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    explicitApi()
    jvm()

    sourceSets {
        commonMain.dependencies {
            // This sample intentionally consumes the extracted module directly.
            // It is the migration canary for downstream headless integrations.
            implementation(project(":foundation"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

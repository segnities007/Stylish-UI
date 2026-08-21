plugins {
    // Foundation is deliberately Compose-free.  It is a Kotlin Multiplatform
    // module so the same reducer/layout/renderer contract can be consumed by
    // Android, JVM, Wasm, and Apple targets before the styled layers migrate.
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

group = "io.github.segnities007"

kotlin {
    explicitApi()

    android {
        namespace = "com.segnities007.stylishui.foundation"
        compileSdk = 37
        minSdk = 26
    }

    jvm()

    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.segnities007.stylishui.androidruntime"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.segnities007.stylishui.androidruntime"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures { compose = true }
}

dependencies {
    // Pin the Android runtime variant explicitly. Kotlin Multiplatform exposes
    // separate API/runtime elements; leaving selection implicit can resolve the
    // metadata-only API jar for a local project dependency and omit common
    // implementation classes from the consumer APK.
    implementation(project(path = ":", configuration = "androidRuntimeElements")) {
        // The runtime smoke screen composes only StylishTheme + StylishDataTable;
        // neither (nor structure/foundation) references material-icons-extended,
        // so the transitive extended-icons dex is pure install/verify/class-load
        // weight in this startup proxy. Restore it if this screen ever renders
        // an icon-bearing component.
        exclude(group = "org.jetbrains.compose.material", module = "material-icons-extended")
    }
    implementation(libs.compose.runtime)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.multiplatform.ui.tooling.preview)
    implementation("androidx.activity:activity-compose:1.13.0")
}

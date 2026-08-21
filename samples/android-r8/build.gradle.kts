plugins {
    // The root build already puts the Android Gradle plugin on the classpath;
    // omitting a second version request avoids Gradle's unknown-version clash.
    id("com.android.application")
}

android {
    namespace = "com.segnities007.stylishui.r8sample"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.segnities007.stylishui.r8sample"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                rootProject.file("proguard/stylish-ui-consumer-rules.pro"),
            )
        }
    }
}

dependencies {
    implementation(project(":"))
}

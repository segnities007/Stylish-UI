import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop") {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    sourceSets {
        getByName("desktopMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                implementation(project(":catalog"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.segnities007.stylishui.website.MainKt"

        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb)
            packageName = "StylishUI Website"
            packageVersion = "1.0.0"
        }
    }
}

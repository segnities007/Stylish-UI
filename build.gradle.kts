plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.binary.compatibility.validator)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dokka)
    alias(libs.plugins.maven.publish)
}

composeCompiler {
    metricsDestination.set(layout.buildDirectory.dir("compose-metrics"))
    reportsDestination.set(layout.buildDirectory.dir("compose-reports"))
}

group = "io.github.segnities007"

version = providers.fileContents(
    rootProject.layout.projectDirectory.file("version.properties")
).asText.map { text ->
    text.lineSequence()
        .map { it.substringBefore("#") }
        .map { it.trim() }
        .filter { it.isNotEmpty() && it.contains("=") }
        .associate { line ->
            val (key, value) = line.split("=", limit = 2)
            key.trim() to value.trim()
        }["version"] ?: "0.1.0"
}.orElse("0.1.0").get()

kotlin {
    explicitApi()

    android {
        namespace = "com.segnities007.stylishui"
        compileSdk = 37
        minSdk = 26
        withHostTest {}
    }

    jvm()

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.uiUtil)
            implementation(libs.compose.multiplatform.ui.tooling.preview)
            implementation(libs.kotlinx.datetime)
            implementation(libs.material.kolor)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            // Android-specific dependencies are kept here.
        }

        jvmMain.dependencies {
            // Desktop-specific dependencies can be added here.
        }

        jvmTest.dependencies {
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(compose.desktop.currentOs)
        }
    }
}

dokka {
    moduleName.set("Stylish UI")
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = "io.github.segnities007",
        artifactId = "stylish-ui",
        version = version.toString(),
    )

    pom {
        name = "Stylish UI"
        description = "Compose Multiplatform design system components for Stylish My Vehicles."
        url = "https://github.com/segnities007/Stylish-UI"
        inceptionYear = "2026"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        developers {
            developer {
                id = "segnities007"
                name = "segnities007"
                url = "https://github.com/segnities007"
            }
        }

        scm {
            url = "https://github.com/segnities007/Stylish-UI"
            connection = "scm:git:git://github.com/segnities007/Stylish-UI.git"
            developerConnection = "scm:git:ssh://git@github.com:segnities007/Stylish-UI.git"
        }
    }
}

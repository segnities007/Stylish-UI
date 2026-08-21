import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

// A raw webpack bundle is not a deployable site by itself: Compose resources
// (fonts and any future images) are resolved relative to the page at runtime.
// Keep the production handoff reproducible by packaging the bundle, entry page,
// and processed resources into one directory.
val wasmProductionExecutable = layout.buildDirectory.dir("kotlin-webpack/wasmJs/productionExecutable")
val wasmProductionResources = layout.buildDirectory.dir("processedResources/wasmJs/main")

tasks.register<Sync>("assembleWasmProductionSite") {
    dependsOn("wasmJsBrowserProductionWebpack")
    from("src/wasmJsMain/resources") {
        include("index.html", "favicon.svg")
    }
    from(wasmProductionExecutable)
    from(wasmProductionResources) {
        // The resource processor also mirrors index.html; the source copy above
        // is the canonical handoff page, so avoid a duplicate entry.
        exclude("index.html", "favicon.svg")
    }
    into(layout.buildDirectory.dir("wasmSite"))
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.segnities007.stylishui.websitewasm.resources"
    generateResClass = always
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "stylishUiWebsite"
        browser {
            commonWebpackConfig {
                outputFileName = "stylish-ui-website.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        wasmJsMain {
            dependencies {
                implementation(project(":catalog"))
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

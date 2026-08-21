pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Stylish-UI"
include(":foundation")
include(":structure")
include(":website")
include(":catalog")
include(":samples:android-r8")
include(":samples:android-runtime")
include(":samples:foundation-consumer")
include(":samples:structure-consumer")
include(":samples:migration-consumer")
include(":samples:adapters")

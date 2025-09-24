// settings.gradle.kts

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // Add JitPack if the library is hosted there
        // maven { url = uri("https://jitpack.io") }
        // Add any other custom Maven repository if needed
        // maven { url = uri("https://url.to.your.custom.repository") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Add JitPack if the library is hosted there
        // maven { url = uri("https://jitpack.io") }
        // Add any other custom Maven repository if needed
        // maven { url = uri("https://url.to.your.custom.repository") }
    }
}

rootProject.name = "Tugas_2"
include(":app")

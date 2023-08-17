pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // FOR Markdown - notes section
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "Feed Five"
include(":app")
 
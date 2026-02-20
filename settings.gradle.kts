pluginManagement {
    includeBuild("build-logic")

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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "gumapp"
include(":app")
include(":gumrun")
include(":gumbooks")
include(":gumovies")
include(":gumnotes")
include(":gumjournals:data")
include(":gumjournals:domain")
include(":gumjournals:presentation")
include(":core:domain")
include(":core:data")
include(":core:presentation")
include(":core:presentation:designsystem")
include(":gumrun:presentation")
include(":gumrun:data")
include(":gumrun:domain")

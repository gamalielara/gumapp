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
include(":gumrjournals:data")
include(":gumrjournals:domain")
include(":gumrjournals:presentation")
include(":core:domain")
include(":core:data")
include(":core:presentation")
include(":core:presentation:designsystem")

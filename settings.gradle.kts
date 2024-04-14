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
    }
}

rootProject.name = "Shogi2023"
include(":app")
include(":domain:entity")
include(":domain:service")
include(":application:usecase")
include(":presentation:core")
include(":presentation:home")
include(":presentation:game")
include(":data:repository")
include(":data:test-repository")
include(":domain:test-entity")

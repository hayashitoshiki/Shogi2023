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
include(":domain:domain-object")
include(":domain:domain-logic")
include(":domain:service")
include(":domain:repository")
include(":domain:test-repository")
include(":domain:test-domain-object")
include(":application:usecase")
include(":application:test-usecase")
include(":presentation:core")
include(":presentation:home")
include(":presentation:game")
include(":data:repository")
include(":data:di")
include(":presentation:test-core")
include(":application:di")
include(":application:usecaseinterface")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // Fallback Maven mirrors for CI stability
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Fallback Maven mirrors for CI stability
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
    }
}

rootProject.name = "AstroStorm"
include(":app")
include(":core:model")
include(":core:common")


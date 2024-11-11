pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        maven("https://repo.sayandev.org/snapshots/")
    }
}

plugins {
    id("org.sayandev.stickynote.settings") version "1.7.100"
}

rootProject.name = "GameTools"
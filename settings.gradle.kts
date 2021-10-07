pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
    
}

rootProject.name = "compose-draggable-list"

include(":android")
include(":desktop")
include(":draggablelist")


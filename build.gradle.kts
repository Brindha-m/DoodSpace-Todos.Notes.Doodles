// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.46")
        // Add the Enigma classpath
        classpath("gradle.plugin.chrisney:enigma:1.0.0.8")
    }
}

plugins {
    id("com.android.application") version "8.1.0-beta03" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("com.android.library") version "8.1.0-beta03" apply false

}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
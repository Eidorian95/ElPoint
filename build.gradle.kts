// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("org.jetbrains.kotlin.android") version "2.1.21" apply false
    id("com.google.dagger.hilt.android") version "2.55" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    alias(libs.plugins.kotlin.compose) apply false

}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}
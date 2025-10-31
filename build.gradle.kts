// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) version "8.5.2" apply false
    alias(libs.plugins.jetbrains.kotlin.android) version "2.2.10" apply false
    id("com.google.gms.google-services") version "4.4.3" apply false
}
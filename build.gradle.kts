buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}



// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.9.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    //Firebaseç
    id("com.google.gms.google-services") version "4.4.1" apply false
}
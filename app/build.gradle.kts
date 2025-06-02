plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //Firebase
    id("com.google.gms.google-services")
}


android {
    namespace = "com.example.proyecto_final_javig"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.proyecto_final_javig"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    //
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.compose.material:material:1.5.1")
    //Navigation
    implementation ("androidx.navigation:navigation-compose:2.7.2")
    //implementation ("androidx.navigation:navigation-compose:2.8.5")

    implementation ("androidx.compose.material:material-icons-extended:1.6.0")
    //
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.1")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.6")
    //
    implementation("io.coil-kt:coil-compose:2.5.0")

    //implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("androidx.ink:ink-geometry-android:1.0.0-alpha01")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-functions-ktx:20.4.0") // Functions


    //Maps
    implementation("com.google.maps.android:maps-compose:2.11.4")
    implementation("com.google.android.gms:play-services-maps:18.1.0")

    //Libreria neumorphism
    implementation("me.nikhilchaudhari:composeNeumorphism:1.0.0-alpha02")
    implementation("io.github.sridhar-sp:neumorphic:0.0.6")

    //ZXING SCANNER
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // Retrofit para consumir API Open Food Facts
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coroutines para llamadas en segundo plano
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Gson para manejar JSON
    implementation("com.google.code.gson:gson:2.9.0")

}
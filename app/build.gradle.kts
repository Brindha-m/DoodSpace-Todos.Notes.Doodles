plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}


// Ksp. plugin for compose navigation
kotlin {
    sourceSets {
        all{
            languageSettings {
                optIn("kotlin.RequiresOptIn")
            }
            main {
                resources.srcDirs("src/main/res/font")
            }

        }
    }
}
android {
    namespace = "com.implementing.cozyspace"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.implementing.cozyspace"
        minSdk = 26
        targetSdk = 34
        versionCode = 37
        versionName = "1.3.25"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        hilt {
            enableAggregatingTask = true
        }

//        //add this in the build.gradle.kts(app) file
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] =
                    "$projectDir/schemas"
            }
        }
        /*
         No need of kapt, just create a kapt and ksp fun and sync and remove it

                kapt {
                    correctErrorTypes = true
                    includeCompileClasspath = false
                }

         */
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


}




dependencies {

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation(platform("androidx.compose:compose-bom:2025.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.hilt:hilt-work:1.2.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.firebase:firebase-config:22.1.0")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    androidTestImplementation(platform("androidx.compose:compose-bom:2025.03.00"))
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.ui:ui-tooling")

    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.13")


    // Compose navigation
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.55")
    ksp("com.google.dagger:hilt-android-compiler:2.55")
    ksp("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-work:1.2.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Coroutines - Light weight thread, asynchronous
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Gson - Json to Java Objects
    implementation("com.google.code.gson:gson:2.10.1")

    // Preference - Datastore
    implementation("androidx.datastore:datastore-preferences:1.1.3")

    // Accompanist - let this be of the same version for permission
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    implementation("com.google.accompanist:accompanist-flowlayout:0.23.1")
    implementation ("com.google.accompanist:accompanist-permissions:0.31.1-alpha")
    //Moshi - modern JSON library for Android
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")

    // Compose MarkDown
    implementation("com.github.jeziellago:compose-markdown:0.3.4")

    implementation("com.github.idapgroup:Snowfall:0.8.2")

//    // Biometric
//    implementation("androidx.biometric:biometric:1.4.0-alpha02")

    // Compose Glance (Widgets)

    // For Glance support
    implementation("androidx.glance:glance:1.1.1")

    // For AppWidgets support
    implementation("androidx.glance:glance-appwidget:1.1.1")

    // For Wear-Tiles support
    implementation("androidx.glance:glance-wear-tiles:1.0.0-alpha05")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.0")
    implementation("androidx.startup:startup-runtime:1.2.0")

    // Compose live data
    implementation("androidx.compose.runtime:runtime-livedata")

    /** Animated Compose Bottom navigation */
    implementation("com.exyte:animated-navigation-bar:1.0.0")

    // Model bottom sheet for task section - time,date picker
    implementation("androidx.compose.material3:material3:1.3.1")

    // color picker for doodle section
    implementation("com.raedapps:alwan:1.0.1")


    implementation("com.mikepenz:multiplatform-markdown-renderer-m3:0.27.0")
    implementation("com.mikepenz:multiplatform-markdown-renderer-coil2:0.27.0")

    implementation("com.github.idapgroup:Snowfall:0.9.10")

    // Firebase Notification
    // Firebase Config for remote updates -- TODOS
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging:24.1.0")


    // Add the dependencies for the In-App Messaging and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-inappmessaging-display")


    // Play Store Update Toast cards
    implementation("com.google.android.play:app-update:2.1.0")

    // For Kotlin users also import the Kotlin extensions library for Play In-App Update:
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    // Rating toast inside the app
    implementation("com.google.android.play:review-ktx:2.0.2")


    //constraint layout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    //    Coil is to load the images
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("io.coil-kt:coil-gif:2.2.2")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

}
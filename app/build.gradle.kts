import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("com.chrisney.enigma")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

enigma.enabled = true
enigma.injectFakeKeys = true

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
    compileSdk = 34

    defaultConfig {
        applicationId = "com.implementing.cozyspace"
        minSdk = 26
        targetSdk = 34
        versionCode = 23
        versionName = "1.0.23"

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
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}




dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.hilt:hilt-work:1.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.firebase:firebase-config:21.5.0")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.ui:ui-tooling")

    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")


    // Compose navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-android-compiler:2.46")
    kapt("androidx.hilt:hilt-compiler:1.1.0")
//    implementation("androidx.hilt:hilt-work:1.0.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")

    // Coroutines - Light weight thread, asynchronous
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Gson - Json to Java Objects
    implementation("com.google.code.gson:gson:2.9.1")

    // Preference - Datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    implementation("com.google.accompanist:accompanist-permissions:0.23.1")
    implementation("com.google.accompanist:accompanist-flowlayout:0.23.1")

    //Moshi - modern JSON library for Android
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")

    // Compose MarkDown
    implementation("com.github.jeziellago:compose-markdown:0.3.4")

    implementation("com.github.idapgroup:Snowfall:0.8.1")


    // Compose Glance (Widgets)

    // For Glance support
    implementation("androidx.glance:glance:1.0.0")

    // For AppWidgets support
    implementation("androidx.glance:glance-appwidget:1.0.0")

    // For Wear-Tiles support
    implementation("androidx.glance:glance-wear-tiles:1.0.0-alpha05")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // Compose live data
    implementation("androidx.compose.runtime:runtime-livedata")

    /** Animated Compose Bottom navigation */
    implementation("com.exyte:animated-navigation-bar:1.0.0")

    // Model bottom sheet for task section
    implementation("androidx.compose.material3:material3:1.1.2")

    // color picker for doodle section
    implementation("com.raedapps:alwan:1.0.1")

    // Firebase Notification
    implementation("com.google.firebase:firebase-messaging:23.3.1")
    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))

    // Add the dependencies for the In-App Messaging and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-inappmessaging-display")
    implementation("com.google.firebase:firebase-analytics")

    // Play Store Update Toast cards
    implementation("com.google.android.play:app-update:2.1.0")

    // For Kotlin users also import the Kotlin extensions library for Play In-App Update:
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    // Rating toast inside the app
    implementation("com.google.android.play:review-ktx:2.0.1")


    // Firebase Config for remote updates -- TODOS
    implementation("com.google.firebase:firebase-config:21.5.0")


    //constraint layout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    //stream sdk
    implementation("io.getstream:stream-chat-android-compose:5.4.0")
    implementation("androidx.compose.material:material-icons-extended:1.4.3")

    // sketchbook
    implementation("io.getstream:sketchbook:1.0.4")

    // network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // image loading
    implementation("com.github.skydoves:landscapist-glide:2.2.12")

    // moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    // konfetti
    implementation("nl.dionsegijn:konfetti-compose:2.0.3")

    // firebase
    implementation("com.google.firebase:firebase-database")

    implementation("com.jakewharton.timber:timber:5.0.1")
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
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
    namespace = "com.implementing.feedfive"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.implementing.feedfive"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
            isMinifyEnabled = false
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

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.hilt:hilt-work:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.ui:ui-tooling")


    // Compose navigation
    implementation("androidx.navigation:navigation-compose:2.7.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-android-compiler:2.46")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
//    implementation("androidx.hilt:hilt-work:1.0.0")

    // Room
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")

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

    // Compose Glance (Widgets)

    // For Glance support
    implementation("androidx.glance:glance:1.0.0-rc01")

    // For AppWidgets support
    implementation("androidx.glance:glance-appwidget:1.0.0-rc01")

    // For Wear-Tiles support
    implementation("androidx.glance:glance-wear-tiles:1.0.0-alpha05")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // Compose live data
    implementation("androidx.compose.runtime:runtime-livedata")

    /** Animated Compose Bottom navigation */
    implementation("com.exyte:animated-navigation-bar:1.0.0")

    // Model bottom sheet for task section
    implementation("androidx.compose.material3:material3:1.1.1")

    // color picker for doodle section
    implementation("com.raedapps:alwan:1.0.1")
}




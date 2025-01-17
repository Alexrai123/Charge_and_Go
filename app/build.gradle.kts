plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    //id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id ("dagger.hilt.android.plugin")
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.licenta_copie"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.licenta_copie"
        minSdk = 26
        targetSdk = 34
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //room
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //pt navigate
    implementation(libs.androidx.navigation.compose)
    //pt google maps
    implementation (libs.androidx.appcompat)
    // Google maps
    implementation (libs.play.services.maps)
    implementation (libs.play.services.location)
    // Google maps for compose
    implementation (libs.maps.compose)
    // KTX for the Maps SDK for Android
    implementation (libs.maps.ktx)
    // KTX for the Maps SDK for Android Utility Library
    implementation (libs.maps.utils.ktx)
    // Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)
    implementation (libs.androidx.core.ktx.v1120)
}
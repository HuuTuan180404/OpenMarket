plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.oumarket"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.oumarket"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.cloudinary.android)
    implementation(libs.ratingBar)
    implementation(libs.play.services.location)
    implementation(libs.cuteToast)
    implementation(libs.android.socialButtons)
    implementation(libs.rey5137)
    implementation(libs.paperdb)
    implementation(libs.materialSearchBar)
    implementation(libs.firebase.auth)
    implementation(libs.sqliteassethelper)
    implementation(libs.design)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.storage)
    implementation(libs.android.iconify.material)
    implementation(libs.android.iconify.fontawesome)
    implementation(libs.picasso)
    implementation(libs.firebase.ui.database)
    implementation(libs.recyclerview.v7)
    implementation(libs.cardview.v7)
    implementation(libs.appcompat.v7)
    implementation(libs.core)
    implementation(libs.firebase.database)
    implementation(libs.materialedittext)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.bom)
//
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.ps.wefriends"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ps.wefriends"
        minSdk = 24
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
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)


    // Dagger Hilt
    implementation(libs.hilt.core)
    implementation(libs.hilt.navigation)
    ksp(libs.hilt.compiler)

    // Lifecycle Aware Flow Collector
    implementation(libs.compose.lifecycle.flow)

    // Compose Navigation
    implementation(libs.compose.navigation)

    // Extended Icons
    implementation(libs.compose.extendedIcons)

    // Google Fonts
    implementation(libs.compose.googleFonts)

    // Splash Screen
    implementation(libs.splashScreen)

    // Firebase Bom
    implementation(platform(libs.firebase.bom))

    // Firebase Authentication
    implementation(libs.firebase.auth.ktx)

    // Firebase Firestore
    implementation(libs.firebase.firestore)

    // Timber - Logger
    implementation(libs.timber)

    // One Tap Auth
    implementation(libs.auth.one.tap)

    // Message Bar
    implementation(libs.message.bar)

    // Lottie Animation
    implementation(libs.lottie.animations)

    // Datastore
    implementation(libs.datastore)
    implementation(libs.serialization)

    // Coil
    implementation(libs.coil)
}
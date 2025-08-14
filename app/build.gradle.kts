plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "tj.ikrom.cinemahall"
    compileSdk = 36

    defaultConfig {
        applicationId = "tj.ikrom.cinemahall"
        minSdk = 24
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    ksp(libs.hilt.compiler)
    ksp(libs.androidx.compiler)
    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)

    implementation(libs.navigation.ui)
    implementation(libs.navigation.ktx)
    implementation(libs.navigation.fragment)

    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)

    implementation(libs.chucker.library)

    implementation(libs.gson)
    implementation(libs.okhttp3)
    implementation(libs.retrofit)
    implementation(libs.okhttp3.logging)
    implementation(libs.retrofit.converter)

    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.material)
    implementation(libs.arcore)
    implementation(libs.annotation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.flatcode.simplemultiapps"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.flatcode.simplemultiapps"
        minSdk = 24
        targetSdk = 37
        versionCode = 8
        versionName = "1.36"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.preference.ktx)           //Shared Preference
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Layout
    implementation(libs.material)
    implementation(libs.multicolors)                   //Theme Colors
    //Image
    implementation(libs.coil)                              //Coil Image
    implementation(libs.coilVideo)                         //Coil Video
    //Pdf Reader
    implementation(libs.appintro)
    implementation(libs.android.pdf.viewer)
    //Live TV
    implementation(libs.media3.hls)
    implementation(libs.media3.datasource)
    //Video Player
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    //News & Wordpress
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //Wordpress
    implementation(libs.androidx.swiperefreshlayout)
    //Video Player
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    //Other's
    implementation(libs.jsoup)
    implementation(libs.volley)
    implementation(libs.gson)
}
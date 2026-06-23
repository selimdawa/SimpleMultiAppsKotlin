plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.flatcode.simplemultiapps"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.flatcode.simplemultiapps"
        minSdk = 24
        targetSdk = 37
        versionCode = 7
        versionName = "1.35"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    //signingConfigs {
    //    create("release") {
    //        storeFile =
    //            file("D:\\MyProjects\\Kotlin\\Simple Multi Apps\\Simple Multi Apps\\SimpleMultiApps.jks")
    //        storePassword = "00000000"
    //        keyAlias = "SimpleMultiApps"
    //        keyPassword = "00000000"
    //    }
    //}
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    //buildTypes {
    //    getByName("release") {
    //        signingConfig = signingConfigs.getByName("release")
    //        isMinifyEnabled = true
    //        isShrinkResources = true
    //        proguardFiles(
    //            getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
    //        )
    //    }
    //}
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        dataBinding = true
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
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    //Image
    implementation(libs.circleimageview)                //Circle Image
    implementation(libs.glide)                          //Glide Image
    //Pdf Reader
    implementation(libs.appintro)
    implementation(libs.android.pdf.viewer)
    implementation(libs.attributionpresenter)
    implementation(libs.whatsnew)
    //Multi delete demo
    implementation(libs.androidx.lifecycle.extensions)
    //Video Player
    implementation(libs.exoplayer)
    implementation(libs.volley)
    //News & Wordpress
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //Wordpress
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.gson)
    //Other's
    implementation(libs.jsoup)
}
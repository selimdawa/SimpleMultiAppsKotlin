plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.flatcode.simplemultiapps"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.flatcode.simplemultiapps"
        minSdk = 24
        targetSdk = 36
        versionCode = 6
        versionName = "1.30"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile =
                file("D:\\MyProjects\\Kotlin\\Simple Multi Apps\\Simple Multi Apps\\SimpleMultiApps.jks")
            storePassword = "00000000"
            keyAlias = "SimpleMultiApps"
            keyPassword = "00000000"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        dataBinding = true
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
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.cardview)
    //Pdf Reader
    implementation(libs.appintro)
    implementation(libs.android.pdf.viewer)
    implementation(libs.attributionpresenter)
    implementation(libs.whatsnew)
    //Image
    implementation(libs.circleimageview)                //Circle Image
    implementation(libs.glide)                          //Glide Image
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.compiler)                                 //Glide Compiler
    //Multi delete demo
    implementation(libs.androidx.lifecycle.extensions)
    //Others
    implementation(libs.jsoup)
    //noinspection GradleDependency
    implementation(libs.exoplayer)     //Video Player
    implementation(libs.volley)
    //News & Wordpress
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //Wordpress
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.gson)
}
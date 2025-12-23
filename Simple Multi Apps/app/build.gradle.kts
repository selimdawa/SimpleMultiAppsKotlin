plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.flatcode.simplemultiapps"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.flatcode.simplemultiapps"
        minSdk = 24
        targetSdk = 36
        versionCode = 5
        versionName = "1.26"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.preference:preference-ktx:1.2.1")           //Shared Preference
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    //Layout
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    //Pdf Reader
    implementation("com.github.paolorotolo:appintro:v5.1.0")
    implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    implementation("com.github.franmontiel:AttributionPresenter:1.0.1")
    implementation("io.github.tonnyl:whatsnew:0.1.7")
    //Image
    implementation("de.hdodenhof:circleimageview:3.1.0")                //Circle Image
    implementation("com.github.bumptech.glide:glide:5.0.5")            //Glide Image
    //noinspection KaptUsageInsteadOfKsp
    kapt("com.github.bumptech.glide:compiler:5.0.5")                   //Glide Compiler
    //Multi delete demo
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    //Others
    implementation("org.jsoup:jsoup:1.21.2")
    //noinspection GradleDependency
    implementation("com.google.android.exoplayer:exoplayer:2.14.2")     //Video Player
    implementation("com.android.volley:volley:1.2.1")
    //News & Wordpress
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    //Wordpress
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.code.gson:gson:2.13.2")
}
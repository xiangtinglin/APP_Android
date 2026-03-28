repositories {
    google()        // 添加 Google 仓库
    mavenCentral()  // 添加 Maven Central 仓库
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "1.8.22"  // Kotlin 插件及其版本
}

android {
    namespace = "com.example.android_os_finalreport"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.android_os_finalreport"
        minSdk = 23
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("androidx.navigation:navigation-ui:2.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
    implementation("com.squareup.okhttp3:okhttp:3.14.9")

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    implementation ("com.google.android.exoplayer:exoplayer:2.19.0")
    implementation ("androidx.media3:media3-exoplayer:1.0.0")
    implementation ("androidx.media3:media3-ui:1.0.0")

    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
}

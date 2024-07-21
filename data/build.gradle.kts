import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "io.github.joelkanyi.data"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // Read API_KEY from environment variable or gradle.properties
        val apiKey: String = System.getenv("API_KEY") ?: gradleLocalProperties(rootDir, providers).getProperty("API_KEY") as String ?: ""
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.paging.common)
    implementation(libs.javax.inject)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson.converter)

    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
    implementation(libs.androidx.junit.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junit)
    testImplementation(libs.truth)
    androidTestImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.coroutines.test)

    // Domain
    implementation(projects.domain)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.timber)

    implementation(libs.androidx.appcompat)
    implementation(libs.datastore.preferences)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    testImplementation(libs.room.testing)

    testImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.core)

    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(libs.turbine)
}

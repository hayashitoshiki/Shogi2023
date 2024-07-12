plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.home"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.foundation)
    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
    androidTestImplementation(libs.androidx.test.espresso)
    // Hilt
    implementation(libs.hilt.navigation.compose)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    // Tab
    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    // coroutine-test
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)

    implementation(project(":presentation:core"))
    implementation(project(":application:usecase"))
    implementation(project(":domain:domain-object"))
    implementation(project(":presentation:game"))

    implementation(project(":application:usecaseinterface"))
    implementation(project(":application:di"))
    testImplementation(project(":domain:test-domain-object"))
    testImplementation(project(":presentation:test-core"))
    testImplementation(project(":application:test-usecase"))
}

kapt {
    correctErrorTypes = true
}
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.usecase"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
}
dependencies {
    // Test
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
    // Hilt
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    implementation(project(":domain:domain-object"))
    implementation(project(":domain:domain-logic"))
    implementation(project(":domain:service"))
    implementation(project(":domain:repository"))
    implementation(project(":application:usecaseinterface"))
    implementation(project(":data:di"))
    testImplementation(project(":domain:test-repository"))
    testImplementation(project(":domain:test-domain-object"))
    testImplementation(project(":domain:test-service"))
}

kapt {
    correctErrorTypes = true
}
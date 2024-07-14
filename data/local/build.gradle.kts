plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  kotlin("kapt")
}

android {
  namespace = "com.example.local"
  compileSdk = 34

  defaultConfig {
    minSdk = 24

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {

  implementation(libs.androidx.core)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  // Test
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.junit)
  androidTestImplementation(libs.androidx.test.espresso)
  // Hilt
  implementation(libs.dagger)
  kapt(libs.dagger.compiler)
  // DataStore
  implementation(libs.datastore.preferences)
  implementation(libs.datastore.preferences.core)

  implementation(project(":domain:domain-object"))
}

kapt {
  correctErrorTypes = true
}

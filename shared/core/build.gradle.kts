plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.experiencetv.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 28
    }

    // Keep Java and Kotlin bytecode targets aligned across every app module.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

// Shared module targets JVM 17 to match both Android applications.

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val stableKeystorePath = System.getenv("ANDROID_KEYSTORE_FILE")?.takeIf { it.isNotBlank() }
val stableKeystoreFile = stableKeystorePath?.let { file(it) }?.takeIf { it.isFile }

android {
    namespace = "com.antigravity.launcher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.antigravity.launcher"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
    }

    signingConfigs {
        if (stableKeystoreFile != null) {
            create("stable") {
                storeFile = stableKeystoreFile
                storeType = "PKCS12"
                storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD") ?: ""
                keyAlias = System.getenv("ANDROID_KEY_ALIAS") ?: "antigravitylauncher"
                keyPassword = System.getenv("ANDROID_KEY_PASSWORD") ?: ""
            }
        }
    }

    buildTypes {
        debug {
            if (stableKeystoreFile != null) {
                signingConfig = signingConfigs.getByName("stable")
            }
        }
        release {
            isMinifyEnabled = false
            if (stableKeystoreFile != null) {
                signingConfig = signingConfigs.getByName("stable")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.browser:browser:1.8.0")
}

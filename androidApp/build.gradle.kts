plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "io.agora.live.livegame.android"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = true
            isDebuggable = false
            lint{
                checkReleaseBuilds = false
            }
        }
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
        }
    }
    buildFeatures{
        compose = true
    }
    // Set both the Java and Kotlin compilers to target Java 8.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
}

dependencies {
    implementation(project(":shared"))

    val verCompose :String by rootProject.extra
//    Compose
    implementation("androidx.compose.ui:ui:$verCompose")
    implementation("androidx.compose.ui:ui-tooling:$verCompose")
    implementation("androidx.compose.material:material:$verCompose")
    implementation("androidx.navigation:navigation-compose:2.4.1")

//    accompanist
    implementation("com.google.accompanist:accompanist-insets:0.23.1")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.23.1")

    // AndroidX
    implementation ("androidx.activity:activity-compose:1.4.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")


//    Agora
    implementation("com.github.AgoraIO-Community:SyncManager-Android:1.0.4")
    implementation("com.github.agorabuilder:agora-full-preview:4.0.0.preview.5")
}
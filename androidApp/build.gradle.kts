import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("/Users/lq/release.keystore")
            val property = gradleLocalProperties(project.rootDir)
            storePassword = property["keystore.storePassword"] as String
            keyAlias = property["keystore.alias"] as String
            keyPassword = property["keystore.keyPassword"] as String
        }
    }
    compileSdk = 31
    defaultConfig {
        applicationId = "io.agora.live.livegame.android"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        signingConfig = signingConfigs.getByName("release")
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
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
    // 处理状态栏、导航栏高度
    implementation("com.google.accompanist:accompanist-insets:0.23.1")
    // 处理状态栏、导航栏颜色
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.23.1")
    // 处理权限
    implementation("com.google.accompanist:accompanist-permissions:0.23.1")

    // AndroidX
    implementation ("androidx.activity:activity-compose:1.4.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("io.coil-kt:coil-compose:2.0.0-rc01")
    implementation("com.squareup.moshi:moshi-kotlin:1.13.0")
}
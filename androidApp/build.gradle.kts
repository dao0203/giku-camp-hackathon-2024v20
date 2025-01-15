plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.dao0203.gikucampv20.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.dao0203.gikucampv20.android"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    lint {
        sarifOutput =
            File(
                project.layout.buildDirectory
                    .get()
                    .asFile,
                "reports/android-lint/lintResults.sarif",
            )
        textOutput =
            File(
                project.layout.buildDirectory
                    .get()
                    .asFile,
                "reports/android-lint/lintResults.txt",
            )
        htmlOutput =
            File(
                project.layout.buildDirectory
                    .get()
                    .asFile,
                "reports/android-lint/lintResults.html",
            )
        xmlReport = false
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.compose.ui.tooling)

    // koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose.viewmodel.navigation)

    // kotlinx-serialization
    implementation(libs.kotlinx.serialization.json)

    // cameraX
    implementation(libs.androidx.camera)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // mediapipe
    implementation(libs.mediapipe)

    // permission
    implementation(libs.accompanist.permissions)

    lintChecks(libs.compose.lint.checks)
}

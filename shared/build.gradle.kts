import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.serialization)
    alias(libs.plugins.skie)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            export(libs.lifecycle.viewmodel) // Swift側からViewModelを使えるようにする
            baseName = "GikuCampKit"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {

            // lifecycle-viewmodel
            api(libs.lifecycle.viewmodel)

            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)

            // kotlinx-datetime
            api(libs.kotlinx.datetime)

            implementation(libs.room.runtime)

            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {

            // lifecycle-viewmodel
            implementation(libs.lifecycle.viewmodel.ktx)
            implementation(libs.lifecycle.viewmodel.compose)

            implementation(libs.koin.android)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

skie {
    features.enableSwiftUIObservingPreview = true
}

android {
    namespace = "com.dao0203.gikucampv20"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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

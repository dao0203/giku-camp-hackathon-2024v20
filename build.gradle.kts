plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.serialization).apply(false)
    alias(libs.plugins.skie).apply(false)
    alias(libs.plugins.spotless)
}


subprojects {
    apply(plugin = "com.diffplug.spotless")
    spotless {
        kotlin {
            ktlint("1.0.0")
            target("**/*.kt")
            target("**/*.kts")
            targetExclude("**/build/**/*.kt")
            targetExclude("**/build/**/*.kts")
        }
    }
}
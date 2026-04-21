plugins {
    alias(libs.plugins.gumapp.android.library)
    alias(libs.plugins.gumapp.jvm.ktor)
}

android {
    namespace = "com.gumrindelwald.network"

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://your-api.com\"")
        buildConfigField("String", "API_KEY", "\"your-api-key-here\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(libs.timber)

    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":gumrun:domain"))
}
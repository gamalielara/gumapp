plugins {
    alias(libs.plugins.gumapp.android.library)
    alias(libs.plugins.gumapp.jvm.ktor)
}

android {
    namespace = "com.gumrindelwald.network"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(libs.timber)

    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":gumrun:domain"))
}
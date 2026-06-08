plugins {
    alias(libs.plugins.gumapp.android.library)
    alias(libs.plugins.gumapp.jvm.ktor)
}

android {
    namespace = "com.gumrindelwald.core.network"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(libs.timber)

    implementation(project(":core:domain"))
}

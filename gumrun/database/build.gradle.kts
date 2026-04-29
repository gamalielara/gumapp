plugins {
    alias(libs.plugins.gumapp.android.library)
    alias(libs.plugins.gumapp.android.room)
}

android {
    namespace = "com.gumrindelwald.gumrun.database"
    compileSdk = 36
}

dependencies {
    implementation(project(":gumrun:domain"))
    implementation(project(":core:domain"))
    implementation(libs.bundles.koin)
}

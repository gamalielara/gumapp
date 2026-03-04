plugins {
    alias(libs.plugins.gumapp.android.library)
}

android {
    namespace = "com.gumrindelwald.location"

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.koin)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.android.gms.play.services.location)

    implementation(project(":core:domain"))
    implementation(project(":gumrun:domain"))
}
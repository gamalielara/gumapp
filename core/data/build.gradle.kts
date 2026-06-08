plugins {
    alias(libs.plugins.gumapp.android.library)
    alias(libs.plugins.gumapp.jvm.ktor)
}

android {
    namespace = "com.gumrindelwald.core.data"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(project(":core:network"))
    implementation(project(":core:domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
}

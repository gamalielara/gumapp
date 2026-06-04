plugins {
    alias(libs.plugins.gumapp.android.library)
    alias(libs.plugins.gumapp.jvm.ktor)
}

android {
    namespace = "com.gumrindelwald.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":gumrun:domain"))
}
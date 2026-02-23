plugins {
    alias(libs.plugins.gumapp.android.library.compose)
    alias(libs.plugins.mapsplatform.secrets.plugin)
}

android {
    namespace = "com.gumrindelwald.gumrun.presentation"
}

dependencies {
    implementation(project(":core:presentation:designsystem"))
    implementation(project(":core:presentation"))

    implementation(libs.google.maps.android.compose)
}

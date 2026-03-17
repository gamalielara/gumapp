plugins {
    alias(libs.plugins.gumapp.android.feature.ui)
    alias(libs.plugins.mapsplatform.secrets.plugin)
}

android {
    namespace = "com.gumrindelwald.gumrun.presentation"
}

dependencies {
    implementation(project(":core:presentation:designsystem"))
    implementation(project(":core:presentation"))
    implementation(project(":gumrun:domain"))
    implementation(project(":core:domain"))

    implementation(libs.google.maps.android.compose)
    implementation(libs.androidx.activity.compose)

}

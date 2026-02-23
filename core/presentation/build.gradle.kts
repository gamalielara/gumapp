plugins {
    alias(libs.plugins.gumapp.android.library.compose)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.gumrindelwald.core.presentation"

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":core:presentation:designsystem"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.material3)
}
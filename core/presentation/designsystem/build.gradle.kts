plugins {
    alias(libs.plugins.gumapp.android.library.compose)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.gumrindelwald.gumapp.core.presentation.designsystem"
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.material3)
}
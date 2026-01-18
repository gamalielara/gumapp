plugins {
    alias(libs.plugins.gumapp.android.library.compose)
}

android {
    namespace = "com.gumrindelwald.gumapp.gumjournals.presentation"
}
dependencies {
    implementation(project(":core:presentation:designsystem"))
}

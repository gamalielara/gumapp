plugins {
    alias(libs.plugins.gumapp.android.library)
    alias(libs.plugins.gumapp.android.room)
}

android {
    namespace = "com.gumrindelwald.database"
    compileSdk = 36

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(project(":core:domain"))
}
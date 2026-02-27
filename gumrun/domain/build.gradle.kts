plugins {
    alias(libs.plugins.gumapp.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(project(":core:domain"))
}
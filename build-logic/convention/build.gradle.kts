plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "gumapp.androidApplication"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "gumapp.androidApplication.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidLibrary") {
            id = "gumapp.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "gumapp.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidFeatureUI") {
            id = "gumapp.android.library.feature.ui"
            implementationClass = "AndroidFeatureUIConventionPlugin"
        }

        register("androidRoom") {
            id = "gumapp.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }

        register("jvmLibrary") {
            id = "gumapp.jvm.library"
            implementationClass = "JVMLibraryConventionPlugin"
        }

        register("jvmKtor") {
            id = "gumapp.jvm.ktor"
            implementationClass = "JVMKtorConventionPlugin"
        }
    }
}
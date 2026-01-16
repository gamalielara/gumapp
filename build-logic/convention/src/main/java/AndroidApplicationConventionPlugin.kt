import com.android.build.api.dsl.ApplicationExtension
import com.gumrindelwald.convention.ExtensionType
import com.gumrindelwald.convention.configureBuildTypes
import com.gumrindelwald.convention.configureKotlinAndroid
import com.gumrindelwald.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = libs.findVersion("projectApplicationId").get().toString()
                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()

                    targetSdk = libs.findVersion("projectTargetSdk").get().toString().toInt()
                    minSdk = libs.findVersion("projectMinSdk").get().toString().toInt()
                    compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
                }

                configureKotlinAndroid(this)
                configureBuildTypes(this, ExtensionType.APPLICATION)
            }

        }
    }
}
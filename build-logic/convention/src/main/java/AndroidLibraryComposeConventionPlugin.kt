import com.android.build.gradle.LibraryExtension
import com.gumrindelwald.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("gumapp.android.library")
            }

            val extensions = extensions.getByType<LibraryExtension>()

            configureAndroidCompose(extensions)
        }
    }
}

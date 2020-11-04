import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.*
import javax.inject.Inject

@CacheableTask
abstract class ReverseFiles @Inject constructor(
        objects: ObjectFactory,
        projectLayout: ProjectLayout
): DefaultTask() {

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    val sourceDirectory: DirectoryProperty = objects.directoryProperty().convention(projectLayout.projectDirectory.dir("src"))

    @OutputDirectory
    val outputDirectory: DirectoryProperty = objects.directoryProperty().convention(projectLayout.buildDirectory.dir("reversed"))

    @TaskAction
    fun reverse() {
        sourceDirectory.get().asFile.listFiles()!!.forEach {
            Thread.sleep(100)
            outputDirectory.file(it.name).get().asFile.writeText(it.readText().reversed())
        }
    }
}
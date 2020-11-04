import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class ReverseFiles: DefaultTask() {

    @InputDirectory
    var sourceDirectory: File = project.projectDir.resolve("src")

    @OutputDirectory
    var outputDirectory: File = project.buildDir.resolve("reversed")

    @TaskAction
    fun reverse() {
        sourceDirectory.listFiles()!!.forEach {
            Thread.sleep(100)
            outputDirectory.resolve(it.name).writeText(it.readText().reversed())
        }
    }
}
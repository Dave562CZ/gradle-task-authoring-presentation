import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class ReverseFiles: DefaultTask() {

    var sourceDirectory: File = project.projectDir.resolve("src")

    var outputDirectory: File = project.buildDir.resolve("reversed")

    @TaskAction
    fun reverse() {
        project.mkdir(outputDirectory)
        sourceDirectory.listFiles()!!.forEach {
            Thread.sleep(100)
            outputDirectory.resolve(it.name).writeText(it.readText().reversed())
        }
    }
}
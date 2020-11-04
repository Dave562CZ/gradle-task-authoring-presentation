import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File

@CacheableTask
abstract class ReverseFiles: DefaultTask() {

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
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
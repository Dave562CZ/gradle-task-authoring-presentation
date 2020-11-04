import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class ReverseFiles: DefaultTask() {

    @TaskAction
    fun reverse() {
        project.run {
            mkdir(buildDir.resolve("reversed"))
            projectDir.resolve("src").listFiles()!!.forEach {
                Thread.sleep(100)
                buildDir.resolve("reversed/${it.name}").writeText(it.readText().reversed())
            }
        }
    }
}
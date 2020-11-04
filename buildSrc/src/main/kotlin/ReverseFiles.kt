import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.*
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.workers.WorkerExecutor
import javax.inject.Inject

@CacheableTask
abstract class ReverseFiles @Inject constructor(
        objects: ObjectFactory,
        projectLayout: ProjectLayout,
        private val workerExecutor: WorkerExecutor
): DefaultTask() {

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    val sourceDirectory: DirectoryProperty = objects.directoryProperty().convention(projectLayout.projectDirectory.dir("src"))

    @OutputDirectory
    val outputDirectory: DirectoryProperty = objects.directoryProperty().convention(projectLayout.buildDirectory.dir("reversed"))

    @TaskAction
    fun reverse() {
        var worker = workerExecutor.noIsolation()
        sourceDirectory.get().asFile.listFiles()!!.forEach { input ->
            worker.submit(ReverseFileAction::class.java) {
                it.inputFile.set(input)
                it.outputFile.set(outputDirectory.file(input.name))
            }
        }
    }
}

abstract class ReverseFileParameters: WorkParameters {
    abstract val inputFile: RegularFileProperty
    abstract val outputFile: RegularFileProperty
}

abstract class ReverseFileAction: WorkAction<ReverseFileParameters> {

    override fun execute() {
        Thread.sleep(100)
        parameters.outputFile.get().asFile.writeText(parameters.inputFile.get().asFile.readText().reversed())
    }
}
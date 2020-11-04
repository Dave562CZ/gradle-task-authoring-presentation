import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileType
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.*
import org.gradle.work.ChangeType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
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
    @Incremental
    @PathSensitive(PathSensitivity.RELATIVE)
    val sourceDirectory: DirectoryProperty = objects.directoryProperty().convention(projectLayout.projectDirectory.dir("src"))

    @OutputDirectory
    val outputDirectory: DirectoryProperty = objects.directoryProperty().convention(projectLayout.buildDirectory.dir("reversed"))

    @TaskAction
    fun reverse(inputChanges: InputChanges) {
        var worker = workerExecutor.noIsolation()
        inputChanges.getFileChanges(sourceDirectory).forEach { change ->
            if (change.fileType == FileType.DIRECTORY) return@forEach
            val outputFile = outputDirectory.file(change.file.name).get().asFile
            if (change.changeType == ChangeType.REMOVED) {
                logger.lifecycle("Deleting file: $outputFile")
                outputFile.delete()
            } else {
                logger.lifecycle("Reversing file: ${change.file} to $outputFile")
                worker.submit(ReverseFileAction::class.java) {
                    it.inputFile.set(change.file)
                    it.outputFile.set(outputFile)
                }
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
plugins {
    base
}
val reverseFiles by tasks.registering(ReverseFiles::class) {
    sourceDirectory.set(projectDir.resolve("inputFiles"))
    outputDirectory.set(buildDir.resolve("outputFiles"))
}
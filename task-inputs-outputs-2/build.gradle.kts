plugins {
    base
}
val reverseFiles by tasks.registering(ReverseFiles::class) {
    sourceDirectory = projectDir.resolve("inputFiles")
    outputDirectory = buildDir.resolve("outputFiles")
}
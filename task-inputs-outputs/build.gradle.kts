
val reverseFiles by tasks.registering {
    doLast {
        mkdir(buildDir.resolve("reversed"))
        projectDir.resolve("src").listFiles().forEach {
            Thread.sleep(100)
            buildDir.resolve("reversed/${it.name}").writeText(it.readText().reversed())
        }
    }
}
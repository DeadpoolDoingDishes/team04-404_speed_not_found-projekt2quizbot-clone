plugins {
    id("com.github.node-gradle.node") version "3.1.1"
}

node {
    version.set("16.20.0")
    download.set(true)
    workDir.set(layout.buildDirectory.dir("nodejs"))
    npmWorkDir.set(layout.buildDirectory.dir("npm"))
    nodeProjectDir.set(projectDir)
}


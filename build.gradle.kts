gradle.startParameter.apply {
    isParallelProjectExecutionEnabled = true
    maxWorkerCount = Runtime.getRuntime().availableProcessors()
}

tasks.register("startAll") {
    group = "application"
    description = "Runs frontend npm install and backend bootRun in parallel"
    dependsOn(
        ":frontend:quizbot:npm_run_start",
        ":backend:bootRun"
    )
}

tasks.register("installAndStartAll") {
    group = "application"
    description = "Runs frontend npm install and backend bootRun in parallel"
    dependsOn(
        ":frontend:quizbot:npm_run_start",
        ":frontend:quizbot:npmInstall",
        ":backend:bootRun"
    )
}
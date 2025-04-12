import com.github.gradle.node.npm.task.NpmTask

plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.github.node-gradle.node") version "3.5.0"
}

group = "ch.zhaw.it.pm2"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
node {
	version.set("16.14.0")
	npmVersion.set("8.3.1")
	download.set(true)
}
tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register("npmStart", NpmTask::class) {
	group = "application"
	description = "Startet das Frontend per npm start"
	workingDir = file("../frontend/quizbot")
	args.set(listOf("start"))
}

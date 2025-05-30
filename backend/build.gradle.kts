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
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	// OkHttp (HTTP client)
	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	// org.json (JSON library)
	implementation("org.json:json:20240303")
}
node {
	version.set("16.14.0")
	npmVersion.set("8.3.1")
	download.set(true)
}
tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.register("stage") {
	dependsOn("build")
}

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
	application
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.jpa") version "1.5.20"

	kotlin("plugin.serialization") version "1.5.21"
}

group = "com.ktor"
version = "0.0.1-SNAPSHOT"
application {
	mainClass.set("com.ktor.ApplicationKt")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.apache.poi:poi-ooxml:5.0.0")

	implementation("io.ktor:ktor-server-core:$ktor_version")
	implementation("io.ktor:ktor-server-netty:$ktor_version")
	implementation("io.ktor:ktor-serialization:$ktor_version")
	implementation("ch.qos.logback:logback-classic:$logback_version")
	implementation("io.arrow-kt:arrow-core:0.13.2")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

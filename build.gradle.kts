val koinVersion = "4.1.0-Beta1"
val logbackVersion = "1.5.12"
val mockkVersion = "1.13.13"

plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("io.ktor.plugin") version "3.0.2"
}

group = "br.com.sample"
version = "1.0.0"

application {
    mainClass.set("br.com.sample.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")

//    implementation("io.ktor:ktor-server-cors")
//    implementation("io.ktor:ktor-server-netty")
//    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-status-pages")
//    implementation("io.ktor:ktor-server-request-validation")
    implementation("io.ktor:ktor-server-content-negotiation")
//    implementation("io.ktor:ktor-client-auth")
//    implementation("io.ktor:ktor-client-apache")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor3:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

//    implementation("org.litote.kmongo:kmongo:$kmongo_version")
//    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")

//    implementation("org.apache.kafka:kafka-clients:$kafka_version")

    testImplementation(kotlin("test"))

//    testImplementation("junit:junit:$junit_version")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")


    testImplementation("io.ktor:ktor-server-test-host-jvm")
//    testImplementation("io.ktor:ktor-server-tests")
//    testImplementation("io.ktor:ktor-serialization-kotlinx-json")

    testImplementation("io.ktor:ktor-client-content-negotiation")
    testImplementation("io.mockk:mockk:$mockkVersion")

    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

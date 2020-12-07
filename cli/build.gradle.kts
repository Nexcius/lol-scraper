
plugins {
    kotlin("jvm") version "1.4.10"
    application
}

repositories {
    jcenter()
    maven {
        url = uri("https://kotlin.bintray.com/kotlinx")
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("it.skrape:skrapeit-core:1.0.0-alpha8")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3")
//    implementation("io.ktor:ktor-client-apache:1.4.1")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "net.nexcius.lol.cli.AppKt"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}

plugins {
    kotlin("multiplatform") version "2.1.10"
    `maven-publish`
}

group = "org.chorus_oss"
version = "1.0-SNAPSHOT"
description = "VarLen"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    linuxX64()
    mingwX64()

    // TODO: Add more supported platforms
    // Open an issue if you want a platform to be added.

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.io)
                implementation(libs.kotlin.stdlib)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}
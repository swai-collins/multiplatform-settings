/*
 * Copyright 2020 Russell Wolf
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.russhwolf.settings.build.standardConfiguration
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.4-jetbrains-3"
}
apply(from = "../gradle/publish.gradle")

standardConfiguration()

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":tests"))

                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.12")
                implementation("androidx.test:core:1.2.0")
                implementation("androidx.test.ext:junit:1.1.1")
                implementation("org.robolectric:robolectric:4.3")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.12")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        targets.withType<KotlinNativeTarget>()["macosX64"].compilations.getByName("main") {
            cinterops.create("dbmInterop") {
                includeDirs(file("src/sdbm").absolutePath)
            }
        }
        targets.withType<KotlinNativeTarget>()["linuxX64"].compilations.getByName("main") {
            cinterops.create("dbmInterop") {
                includeDirs(file("src/sdbm").absolutePath)
            }
        }
    }
}

android {
    testOptions.unitTests.isIncludeAndroidResources = true
}

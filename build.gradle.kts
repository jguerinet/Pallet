/*
 * Copyright (c) 2018-2019 Julien Guerinet
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

/*
 * Copyright (c) 2018-2019 Julien Guerinet
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

/*
 * Copyright (c) 2018-2019 Julien Guerinet
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

/*
 * Copyright (c) 2018-2019 Julien Guerinet
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

object Version {
    const val teamCityDsl = "2019.1"
    const val teamCityPlugins = "1.0-SNAPSHOT"
}

plugins {
    java
    kotlin("jvm") version "1.3.31"
    maven
    id("com.github.ben-manes.versions") version "0.21.0"
}

apply(from = "https://raw.githubusercontent.com/jguerinet/Gradle-Artifact-Scripts/master/spotless.gradle")

group = "com.guerinet"
version = "5.0.1"

buildscript {

    repositories {
        jcenter()
    }

    dependencies {
        classpath("com.diffplug.spotless:spotless-plugin-gradle:3.23.0")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.21.0")
    }
}

repositories {
    mavenCentral()
    maven("http://download.jetbrains.com/teamcity-repository")
    maven("https://teamcity.jetbrains.com/app/dsl-plugins-repository")
    maven("https://teamcity.internal.company.com/app/dsl-plugins-repository")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("script-runtime"))
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin:${Version.teamCityDsl}")
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin-bundled:${Version.teamCityPlugins}")
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin-commandLineRunner:${Version.teamCityPlugins}")
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin-commit-status-publisher:${Version.teamCityPlugins}")
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin-gradle:${Version.teamCityPlugins}")
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin-jetbrains.git:${Version.teamCityPlugins}")
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin-pull-requests:${Version.teamCityPlugins}")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

apply(from = "https://raw.githubusercontent.com/jguerinet/Gradle-Artifact-Scripts/master/kotlin-artifacts.gradle")
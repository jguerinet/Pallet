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

package com.guerinet.pallet

import jetbrains.buildServer.configs.kotlin.v2018_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildSteps
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script

/**
 * Android specific BuildStep extensions
 * @author Julien Guerinet
 * @since 1.0.0
 */

/**
 * Returns a build step to increment the build number on Android
 */
fun BuildSteps.incrementBuildAndroid(): BuildStep = gradle {
    name = "Increment Build Number"
    tasks = "incrementBuildNumber"
}

/**
 * Returns a build step to run a Gradle command. Displays the [name] and runs the [task] (defaults to the name)
 */
fun BuildSteps.gradle(name: String, task: String = name.toLowerCase()) = gradle {
    this.name = name
    tasks = task
}

/**
 * Returns a build step to assemble the APK with the [title] on Android
 */
fun BuildSteps.assembleAndroid(title: String): BuildStep = gradle {
    name = "Assemble APK"
    tasks = "clean assemble$title"
}

/**
 * Returns a build step to run unit tests for a [configuration] (defaults to debug) on Android
 */
fun BuildSteps.unitTestsAndroid(configuration: String = "debug"): BuildStep = gradle {
    name = "Unit Tests"
    tasks = "test${configuration.capitalize()}UnitTest"
}

/**
 * Returns a build step to run instrumentation tests for a [configuration] (defaults to debug) on Android
 */
fun BuildSteps.instrumentationTestsAndroid(configuration: String = "debug"): BuildStep = gradle {
    name = "Run Espresso Tests"
    tasks = "connected${configuration.capitalize()}AndroidTest"
}

/**
 * Returns a build step to extract the current version on Android
 */
fun BuildSteps.extractAndroid(title: String): BuildStep = script {
    name = "Extract Version"
    scriptContent = """
        # Get the latest build tools
        export BUILD_TOOLS=${'$'}(${'$'}ANDROID_HOME/tools/bin/sdkmanager --list | grep "build-tools/" | awk '{ print ${'$'}3 }' | tail -1)

        # Grab the version name from the generated APK
        export VERSION=${'$'}(${'$'}ANDROID_HOME/build-tools/${'$'}BUILD_TOOLS/aapt dump badging app/build/outputs/apk/$title/app-$title.apk | grep versionName | awk '{print ${'$'}4}' | grep -o [0-9].*[0-9])

        echo "##teamcity[setParameter name='env.version' value='${'$'}VERSION']"
    """.trimIndent()
}
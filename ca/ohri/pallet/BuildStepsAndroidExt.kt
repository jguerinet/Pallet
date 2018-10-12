/*
 * Copyright 2015-2018 Ottawa mHealth. All rights reserved.
 */

package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildStep
import jetbrains.buildServer.configs.kotlin.v2018_1.BuildSteps
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.script

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
 * Returns a build step to assemble the APK with the [title] on Android
 */
fun BuildSteps.assembleAndroid(title: String): BuildStep = gradle {
    name = "Assemble APK"
    tasks = "clean assemble$title"
}

/**
 * Returns a build step to test an APK on Android
 */
fun BuildSteps.testAndroid(): BuildStep = gradle {
    name = "Run Espresso Tests"
    tasks = "connectedAndroidTest"
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

        echo "##pallet[setParameter name='env.version' value='${'$'}VERSION']"
    """.trimIndent()
}
/*
 * Copyright 2015-2018 Ottawa mHealth. All rights reserved.
 */

package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildStep
import jetbrains.buildServer.configs.kotlin.v2018_1.BuildSteps
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.gradle

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
 * Returns a build step to test an APK on Android
 */
fun BuildSteps.testAndroid(): BuildStep = gradle {
    name = "Run Espresso Tests"
    tasks = "connectedAndroidTest"
}
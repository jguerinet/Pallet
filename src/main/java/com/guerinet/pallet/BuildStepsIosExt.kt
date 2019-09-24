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
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script

/**
 * iOS specific BuildStep extensions
 * @author Julien Guerinet
 * @since 1.0.0
 */

/**
 * Returns a build step to increment the build number on iOS using the [infoPlistPath] to get the Info.plist file
 *  [infoPlistPath] should be similar to path/to/Info.plist
 */
fun BuildSteps.incrementBuildIos(infoPlistPath: String): BuildStep = script {
    name = "Increment Build Number"
    scriptContent = """
        buildNumber=${'$'}(/usr/libexec/PlistBuddy -c "Print CFBundleVersion" $infoPlistPath)
        buildNumber=${'$'}((${'$'}buildNumber + 1))
        /usr/libexec/PlistBuddy -c "Set :CFBundleVersion ${'$'}buildNumber" $$infoPlistPath
    """.trimIndent()
}

/**
 * Returns a build step to extract the current version on iOS using the [infoPlistPath] to get the Info.plist file
 *  [infoPlistPath] should be similar to path/to/Info.plist
 */
fun BuildSteps.extractIos(infoPlistPath: String, isBuildNumberIncluded: Boolean = false): BuildStep = script {
    name = "Extract Version"
    scriptContent = """
        # Grab the build number and build name
        buildVersion=${'$'}(/usr/libexec/PlistBuddy -c "Print CFBundleShortVersionString" $infoPlistPath)

        VERSION=""
        if [ "$isBuildNumberIncluded" = true ]; then
            buildNumber=${'$'}(/usr/libexec/PlistBuddy -c "Print CFBundleVersion" $infoPlistPath)
            VERSION="${'$'}buildVersion.${'$'}buildNumber"
        else
            VERSION="${'$'}buildVersion"
        fi


        echo "##teamcity[setParameter name='env.version' value='${'$'}VERSION']"
    """.trimIndent()
}

/**
 * Returns a build step to run Carthage on iOS
 */
fun BuildSteps.carthageIos(): BuildStep = script {
    name = "Carthage"
    scriptContent = "carthage bootstrap"
}

/**
 * Returns a build step to install the pods on iOS
 */
fun BuildSteps.podsIos(): BuildStep = fastlane("pods", "Pod Install")

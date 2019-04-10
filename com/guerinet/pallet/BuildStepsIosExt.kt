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
 * Returns a build step to increment the build number on iOS
 */
fun BuildSteps.incrementBuildIos(): BuildStep = script {
    name = "Increment Build Number"
    scriptContent = """
        INFOPLIST=${'$'}(xcodebuild -showBuildSettings | grep -i 'INFOPLIST_FILE')
        INFOPLIST_FILE=${'$'}{INFOPLIST/INFOPLIST_FILE = /}

        buildNumber=${'$'}(/usr/libexec/PlistBuddy -c "Print CFBundleVersion" ${'$'}INFOPLIST_FILE)
        buildNumber=${'$'}((${'$'}buildNumber + 1))
        /usr/libexec/PlistBuddy -c "Set :CFBundleVersion ${'$'}buildNumber" ${'$'}INFOPLIST_FILE
    """.trimIndent()
}

/**
 * Returns a build step to extract the current version on iOS
 */
fun BuildSteps.extractIos(isBuildNumberIncluded: Boolean = false): BuildStep = script {
    name = "Extract Version"
    scriptContent = """
        # Grab the build number and build name
        INFOPLIST=${'$'}(xcodebuild -showBuildSettings | grep -i 'INFOPLIST_FILE')
        INFOPLIST_FILE=${'$'}{INFOPLIST/INFOPLIST_FILE = /}
        buildVersion=${'$'}(/usr/libexec/PlistBuddy -c "Print CFBundleShortVersionString" ${'$'}INFOPLIST_FILE)

        VERSION=""
        if [ "$isBuildNumberIncluded" = true ]; then
            buildNumber=${'$'}(/usr/libexec/PlistBuddy -c "Print CFBundleVersion" ${'$'}INFOPLIST_FILE)
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
fun BuildSteps.podsIos(): BuildStep = script {
    name = "Pod Install"
    scriptContent = "bundle exec fastlane pods"
}

/*
 * Copyright (c) 2018-2020 Julien Guerinet
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

import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.DslContext
import jetbrains.buildServer.configs.kotlin.v2018_2.Id
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs

/**
 * Different conventional build types
 */
object Type {

    const val MERGES = "Merges"
    const val DEV = "Dev"
    const val QA = "QA"
    const val CANARY = "Canary"
    const val ALPHA = "Alpha"
    const val BETA = "Beta"
    const val DEMO = "Demo"
    const val RELEASE = "Release"
}

/**
 * Creates a BuildType for the [title] (which is used as the Id and the name). The optional
 *  [shouldTriggerOnCommit] determines whether a new commit to a watched branch should trigger a
 *  build. The optional [referenceName] is used to set up a branch trigger, as is [excludedUsernamesFromTrigger].
 *  [vcsRootId] tells us what Vcs root to use, defaults to the settings root. [shouldBuildDefaultBranch] tells us
 *  whether the default branch should be listened to or not. [isCleanCheckout] determines whether the files in the build
 *  folder should be cleared before continuing (defaults to false). The optional [artifactsPath] is the path to the
 *  generated artifacts. The [artifactBuilds] is the number of builds to keep the artifacts for
 *  (defaults to 10). The [historyBuilds] is the number of builds to keep the build histories for
 *  (defaults to 50). The [init] block allows you to initialize other things, such as build
 *  features and steps.
 */
fun Project.build(
    title: String,
    gitKey: String,
    vcsRootId: Id,
    shouldTriggerOnCommit: Boolean = true,
    referenceName: String = "",
    excludedUsernamesFromTrigger: List<String> = listOf(),
    shouldBuildDefaultBranch: Boolean = false,
    isCleanCheckout: Boolean = false,
    artifactsPath: String? = null,
    artifactBuilds: Int = 10,
    historyBuilds: Int = 50,
    init: BuildType.() -> Unit
): BuildType {

    return buildType {
        // Set the Id and the name based on the title
        id(title)
        name = title

        // Always only allow 1 running build
        maxRunningBuilds = 1

        // Always allow the external status widget
        allowExternalStatus = true

        // Set up the artifacts if we have it
        if (artifactsPath != null) {
            artifactRules = "+:$artifactsPath"
        }

        // Always add the version parameter, even if unused
        params {
            param("env.version", "0.0.0")
        }

        // Attack this VCS url
        vcs {
            root(vcsRootId)
            DslContext.settingsRoot
            if (!shouldBuildDefaultBranch) {
                // If we shouldn't build the default branch, add the filter to remove it
                branchFilter = """
                +:*
                -:<default>
                """.trimIndent()
            }
            cleanCheckout = isCleanCheckout
        }

        triggers {
            if (shouldTriggerOnCommit) {
                vcs {
                    // Always set up the Git trigger to not start if the excluded usernames commit
                    triggerRules = excludedUsernamesFromTrigger.joinToString("\n") { "-:user=$it:**" }
                    branchFilter = referenceName
                }
            }
        }

        cleanup {
            artifacts(builds = artifactBuilds)
            history(builds = historyBuilds)
        }

        init()

        // Set up the features from the base list and the settings
        features {
            commitStatusPublisher {
                publisher = github {
                    githubUrl = "https://api.github.com"
                    authType = personalToken {
                        token = gitKey
                    }
                }
            }
        }
    }
}

/**
 * Creates and returns the path to the android artifacts for the [buildType]
 */
fun artifactsAndroid(buildType: String) = "app/build/outputs/apk/$buildType/*.apk"

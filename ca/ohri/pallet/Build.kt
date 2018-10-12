/*
 * Copyright 2015-2018 Ottawa mHealth. All rights reserved.
 */

package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_1.DslContext
import jetbrains.buildServer.configs.kotlin.v2018_1.Id
import jetbrains.buildServer.configs.kotlin.v2018_1.Project
import jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2018_1.triggers.vcs

/**
 * Different conventional build types
 */
object Type {

    const val CANARY = "Canary"
    const val ALPHA = "Alpha"
    const val RELEASE = "Release"
}

/**
 * Creates a BuildType for the [title] (which is used as the Id and the name). The optional
 *  [shouldTriggerOnCommit] determines whether a new commit to a watched branhc should trigger a
 *  build. The optional [referenceName] is used to set up a branch trigger. [vcsRootId] tells us
 *  what Vcs root to use, defaults to the settings root. [shouldBuildDefaultBranch] tells us whether
 *  the default branch should be listened to or not. The optional [artifactsPath] is the path to the
 *  generated artifacts. The [artifactBuilds] is the number of builds to keep the artifacts for
 *  (defaults to 10). The [historyBuilds] is the number of builds to keep the build histories for
 *  (defaults to 50). The [init] block allows you to initialize other things, such as build
 *  features and steps.
 */
fun Project.build(title: String,
        commitStatusPublisherKey: String,
        shouldTriggerOnCommit: Boolean = true,
        referenceName: String = "",
        vcsRootId: Id = DslContext.settingsRoot.id!!,
        shouldBuildDefaultBranch: Boolean = false,
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
            buildDefaultBranch = shouldBuildDefaultBranch
        }


        triggers {
            if (shouldTriggerOnCommit) {
                vcs {
                    // Always set up the Git trigger to not start if mHealthAdmin commits
                    triggerRules = """
                        -:user=mhealthadmin:**
                        -:user=mhealthteam:**
                        -:user=mhealthadmin <mhealthteam@toh.ca>:**
                    """.trimIndent()

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
                        token = commitStatusPublisherKey
                    }
                }
            }
        }
    }
}

/**
 * Creates and returns the reference to the branch with [branchName]
 */
fun branch(branchName: String) = "+:refs/heads/$branchName"

/**
 * Creates and returns the reference to a pull request to the default branch
 */
fun pr() = "+:refs/pull/*/merge"

/**
 * Creates and returns the path to the android artifacts for the [buildType]
 */
fun artifactsAndroid(buildType: String) = "app/build/outputs/apk/$buildType/*.apk"
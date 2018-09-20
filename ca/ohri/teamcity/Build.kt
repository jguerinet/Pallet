package ca.ohri.teamcity

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_1.Project
import jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2018_1.project
import jetbrains.buildServer.configs.kotlin.v2018_1.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_1.ui.add

/**
 * Different conventional build types
 */
object Type {
    const val CANARY = "canary"
    const val ALPHA = "alpha"
    const val RELEASE = "release"
}

/**
 * Creates a BuildType for the [title] (which is used as the Id and the name). The optional
 *  [referenceName] is used to set up the trigger. The optional [artifactsPath] is the path to the
 *  generated artifacts. The [artifactBuilds] is the number of builds to keep the artifacts for
 *  (defaults to 10). The [historyBuilds] is the number of builds to keep the build histories for
 *  (defaults to 50). The [init] block allows you to initialize other things, such as build
 *  features and steps.
 */
fun Project.build(title: String, referenceName: String? = null, artifactsPath: String? = null,
        artifactBuilds: Int = 10, historyBuilds: Int = 50, init: BuildType.() -> Unit): BuildType {

    return buildType {
        // Set the Id and the name based on the title
        id(title)
        name = title.capitalize()

        // Always only allow 1 running build
        maxRunningBuilds = 1

        // Set up the artifacts if we have it
        if (artifactsPath != null) {
            artifactRules = "+:$artifactsPath"
        }

        // Always add the version parameter, even if unused
        params {
            add {
                param("env.version", "0.0.0")
            }
        }

        // Always set up the Git trigger to not start if mHealthAdmin commits
        triggers {
            vcs {
                triggerRules = """
                    -:user=mHealthAdmin:**
                    -:user=mhealthteam:**
                """.trimIndent()

                // If there's a reference name, use it
                if (referenceName != null) {
                    branchFilter = "+:refs/$referenceName"
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

            add {
                // Always add the commit status publisher
                commitStatusPublisher {
                    publisher = github {
                        githubUrl = "https://api.github.com"
                        authType = personalToken {
                            token = "credentialsJSON:065c9ab8-954d-476f-88cc-8cfd840b1c2f"
                        }
                    }
                }
            }

            add {
                // TODO Always add the YouTrack connection
//                youtrack {
//                    id = "PROJECT_EXT_2"
//                    displayName = "YouTrack"
//                    host = "https://ottawamhealth.myjetbrains.com/youtrack/"
//                    userName = "ottawamhealth"
//                    password = "credentialsJSON:4cc674bd-bc29-40f2-9a4d-5eca83b4c871"
//                    projectExtIds = ""
//                    useAutomaticIds = true
//                }
            }
        }
    }
}

/**
 * Creates and returns the reference to the branch with [branchName]
 */
fun branch(branchName: String) = "heads/$branchName"

/**
 * Creates and returns the reference to a pull request to the [branchName]
 */
fun pr(branchName: String) = "pull/$branchName/merge"

/**
 * Creates and returns the path to the android artifacts for the [buildType]
 */
fun artifactsAndroid(buildType: String) = "app/build/outputs/apk/$buildType/*.apk"
package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildFeature
import jetbrains.buildServer.configs.kotlin.v2018_1.BuildFeatures
import jetbrains.buildServer.configs.kotlin.v2018_1.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_1.Id
import jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures.vcsLabeling
import jetbrains.buildServer.configs.kotlin.v2018_1.triggers.vcs

class PalletBuildType(
    val buildType: String,
    gitKey: String,
    val vcsRootId: Id,
    shouldTriggerOnCommit: Boolean = true,
    referenceName: String = "",
    shouldBuildDefaultBranch: Boolean = false,
    artifactsPath: String? = null,
    artifactBuilds: Int = 10,
    historyBuilds: Int = 50,
    init: PalletBuildType.() -> Unit
) : BuildType() {

    init {
        // Set the Id and the name based on the title
        id(buildType)
        name = buildType

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

        // Attach this VCS url
        vcs {
            root(this@PalletBuildType.vcsRootId)
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
                        token = gitKey
                    }
                }
            }
        }
    }

    /**
     * Returns a BuildFeature to add a git tag with the version if the build is successful
     */
    fun BuildFeatures.tag(): BuildFeature = vcsLabeling {
        this.vcsRootId = vcsRootId
        labelingPattern = "%env.version%"
        successfulOnly = true
    }

    /**
     * Returns a BuildFeature to only run a job if the PR \is targeting the [targetBranch] (defaults to [Git.DEVELOP])
     */
    fun BuildFeatures.gitHubPr(targetBranch: String = Git.DEVELOP) =
        feature {
            type = "pullRequests"
            param("filterAuthorRole", "MEMBER")
            param("vcsRootId", this@PalletBuildType.vcsRootId.value)
            param("authenticationType", "vcsRoot")
            param("filterTargetBranch", Git.getHeadsRef(targetBranch))
        }
}
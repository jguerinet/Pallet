package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.*
import jetbrains.buildServer.configs.kotlin.v2018_1.Id
import jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures.vcsLabeling
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.script
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

    /* Build Features */

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

    /* Build Steps */

    /**
     * Returns a build step to deploy the current version using the build type
     */
    fun BuildSteps.deploy(): BuildStep = script {
        name = "Deploy"
        scriptContent = "bundle exec fastlane ${this@PalletBuildType.buildType.toLowerCase()}"
    }

    /* Build Steps - Android */

    /**
     * Returns a build step to assemble the APK on Android
     */
    fun BuildSteps.assembleAndroid(): BuildStep = gradle {
        name = "Assemble APK"
        tasks = "clean assemble${this@PalletBuildType.buildType}"
    }

    /**
     * Returns a build step to extract the current version on Android
     */
    fun BuildSteps.extractAndroid(): BuildStep = script {
        val title = this@PalletBuildType.buildType

        name = "Extract Version"
        scriptContent = """
        # Get the latest build tools
        export BUILD_TOOLS=${'$'}(${'$'}ANDROID_HOME/tools/bin/sdkmanager --list | grep "build-tools/" | awk '{ print ${'$'}3 }' | tail -1)

        # Grab the version name from the generated APK
        export VERSION=${'$'}(${'$'}ANDROID_HOME/build-tools/${'$'}BUILD_TOOLS/aapt dump badging app/build/outputs/apk/$title/app-$title.apk | grep versionName | awk '{print ${'$'}4}' | grep -o [0-9].*[0-9])

        echo "##teamcity[setParameter name='env.version' value='${'$'}VERSION']"
    """.trimIndent()
    }
}
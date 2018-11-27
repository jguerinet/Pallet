/*
 * Copyright 2015-2018 Ottawa mHealth. All rights reserved.
 */

package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_1.Id

/**
 * Different conventional build types
 */
object Type {

    const val MERGES = "Merges"
    const val CANARY = "Canary"
    const val ALPHA = "Alpha"
    const val RELEASE = "Release"
}

/**
 * Creates a BuildType for the [buildType] (which is used as the Id and the name). The optional
 *  [shouldTriggerOnCommit] determines whether a new commit to a watched branhc should trigger a
 *  build. The optional [referenceName] is used to set up a branch trigger. [vcsRootId] tells us
 *  what Vcs root to use, defaults to the settings root. [shouldBuildDefaultBranch] tells us whether
 *  the default branch should be listened to or not. The optional [artifactsPath] is the path to the
 *  generated artifacts. The [artifactBuilds] is the number of builds to keep the artifacts for
 *  (defaults to 10). The [historyBuilds] is the number of builds to keep the build histories for
 *  (defaults to 50). The [init] block allows you to initialize other things, such as build
 *  features and steps.
 */
fun PalletProject.build(
    buildType: String,
    vcsRootId: Id,
    shouldTriggerOnCommit: Boolean = true,
    referenceName: String = "",
    shouldBuildDefaultBranch: Boolean = false,
    artifactsPath: String? = null,
    artifactBuilds: Int = 10,
    historyBuilds: Int = 50,
    init: PalletBuildType.() -> Unit
): BuildType =
    PalletBuildType(
        buildType, gitKey, vcsRootId, shouldTriggerOnCommit, referenceName, shouldBuildDefaultBranch,
        artifactsPath, artifactBuilds, historyBuilds, init
    )

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
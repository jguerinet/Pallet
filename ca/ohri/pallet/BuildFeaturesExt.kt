/*
 * Copyright 2015-2018 Ottawa mHealth. All rights reserved.
 */

package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildFeature
import jetbrains.buildServer.configs.kotlin.v2018_1.BuildFeatures
import jetbrains.buildServer.configs.kotlin.v2018_1.Id
import jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures.vcsLabeling

/**
 * Generic BuildFeature extensions
 * @author Julien Guerinet
 * @since 1.0.0
 */

/**
 * Returns a BuildFeature to add a git tag with the version if the build is successful
 */
fun BuildFeatures.tag(vcsRootId: Id): BuildFeature = vcsLabeling {
    this.vcsRootId = vcsRootId.value
    labelingPattern = "%env.version%"
    successfulOnly = true
}

/**
 * Returns a BuildFeature to only run a job if the PR on the [vcsRootId] is targeting the
 *  [targetBranch] (defaults to [Git.DEVELOP])
 */
fun BuildFeatures.gitHubPr(vcsRootId: Id, targetBranch: String = Git.DEVELOP): BuildFeature =
    feature {
        type = "pullRequests"
        param("filterAuthorRole", "MEMBER")
        param("vcsRootId", vcsRootId.value)
        param("authenticationType", "vcsRoot")
        param("filterTargetBranch", Git.getHeadsRef(targetBranch))
    }
/*
 * Copyright 2015-2018 Ottawa mHealth. All rights reserved.
 */

package com.guerinet.pallet

import jetbrains.buildServer.configs.kotlin.v2018_2.BuildFeature
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildFeatures
import jetbrains.buildServer.configs.kotlin.v2018_2.Id
import jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures.vcsLabeling

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
 * Returns a BuildFeature to only run a job if the PR is targeting the [targetBranch] (defaults to [Git.DEVELOP])
 */
fun BuildFeatures.gitHubPr(targetBranch: String = Git.DEVELOP): BuildFeature =
    pullRequests {
        provider = github {
            authType = vcsRoot()
            filterTargetBranch = Git.addHeadsRef(targetBranch)
            filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER
        }
    }
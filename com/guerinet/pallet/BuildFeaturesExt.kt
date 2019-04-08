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
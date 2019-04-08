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

import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import jetbrains.buildServer.configs.kotlin.v2018_2.RelativeId
import jetbrains.buildServer.configs.kotlin.v2018_2.VcsRoot
import jetbrains.buildServer.configs.kotlin.v2018_2.project
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

/**
 * Prepares a project by adding the 2 Vcs roots needed for GitFlow
 */
fun palletProject(url: String, username: String, gitKey: String, init: Project.() -> Unit) =
    project {
        init()

        // Add the develop Vcs root and the releases/hotfixes Vcs Root
        developVcsRoot(url, username, gitKey)
        releasesHotfixesVcsRoot(url, username, gitKey)
    }

/**
 * Sets the order of the Build Types based on the order of the [ids]
 */
fun Project.setOrder(vararg ids: String) {
    buildTypesOrderIds = ids.map { RelativeId(it) }
}

/**
 * Creates a Git Vcs root using the [id] as the Id and name, the Git [url], and the [branchSpec] to
 *  listen to. It uses the [branch] as the default branch (null if it's the Git default branch).
 *  The [authPassword] is used to sign into Git
 */
fun gitVcsRoot(
    id: String,
    url: String,
    authUsername: String,
    authPassword: String,
    branchSpec: String? = null,
    branch: String? = null
): VcsRoot = GitVcsRoot {
    id(id)
    name = id
    this.url = url
    if (branch != null) {
        this.branch = Git.getHeadsRef(branch)
    }
    if (branchSpec != null) {
        this.branchSpec = branchSpec
    }
    authMethod = password {
        userName = authUsername
        password = authPassword
    }
    userNameStyle = GitVcsRoot.UserNameStyle.FULL
}

/**
 * Adds a Git Vcs root for the releases/hotfixes. Uses the Git [url], the [authUsername], and the [authPassword]
 *  to set this up. Calls [gitVcsRoot]
 */
fun Project.releasesHotfixesVcsRoot(
    url: String,
    authUsername: String,
    authPassword: String
) = vcsRoot(
    gitVcsRoot(
        Id.RELEASES_HOTFIXES,
        url,
        authUsername,
        authPassword,
        """
    ${Git.getHeadsRef(Git.RELEASES)}
    ${Git.getHeadsRef(Git.HOTFIXES)}
    """.trimIndent(),
        Git.MASTER
    )
)

/**
 * Adds a Git Vcs root for the develop branch. Uses the [url], the [authUsername], and the [authPassword].
 *  Calls [gitVcsRoot]
 */
fun Project.developVcsRoot(url: String, authUsername: String, authPassword: String) =
    vcsRoot(
        gitVcsRoot(
            Id.DEVELOP,
            url,
            authUsername,
            authPassword,
            branch = Git.DEVELOP
        )
    )
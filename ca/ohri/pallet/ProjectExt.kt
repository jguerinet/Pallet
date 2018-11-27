/*
 * Copyright 2015-2018 Ottawa mHealth. All rights reserved.
 */

package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.Project
import jetbrains.buildServer.configs.kotlin.v2018_1.RelativeId
import jetbrains.buildServer.configs.kotlin.v2018_1.VcsRoot
import jetbrains.buildServer.configs.kotlin.v2018_1.vcs.GitVcsRoot

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
    authPassword: String,
    branchSpec: String,
    branch: String? = null
): VcsRoot = GitVcsRoot {
    id(id)
    name = id
    this.url = url
    if (branch != null) {
        this.branch = Git.getHeadsRef(branch)
    }
    this.branchSpec = branchSpec
    authMethod = password {
        userName = Git.USERNAME
        password = authPassword
    }
    userNameStyle = GitVcsRoot.UserNameStyle.FULL
}

/**
 * Creates the Git Vcs root for the releases/hotfixes. Uses the Git [url], the [authPassword], and the [branch] to
 *  set this up. Calls [gitVcsRoot]
 */
fun gitReleasesHotfixesVcsRoot(
    url: String,
    authPassword: String,
    branch: String? = null
): VcsRoot = gitVcsRoot(
    Id.RELEASES_HOTFIXES,
    url,
    authPassword,
    """
    ${Git.getHeadsRef(Git.RELEASES)}
    ${Git.getHeadsRef(Git.HOTFIXES)}
    """.trimIndent(),
    branch
)
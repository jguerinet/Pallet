/*
 * Copyright 2015-2018 Ottawa mHealth. All rights reserved.
 */

package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.Project
import jetbrains.buildServer.configs.kotlin.v2018_1.RelativeId
import jetbrains.buildServer.configs.kotlin.v2018_1.VcsRoot
import jetbrains.buildServer.configs.kotlin.v2018_1.project
import jetbrains.buildServer.configs.kotlin.v2018_1.vcs.GitVcsRoot

/**
 * Prepares a project by adding the 2 Vcs roots needed for GitFlow
 */
fun palletProject(url: String, gitKey: String, init: Project.() -> Unit) =
    project {
        init()

        // Add the develop Vcs root and the releases/hotfixes Vcs Root
        developVcsRoot(url, gitKey)
        releasesHotfixesVcsRoot(url, gitKey)
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
        userName = Git.USERNAME
        password = authPassword
    }
    userNameStyle = GitVcsRoot.UserNameStyle.FULL
}

/**
 * Adds a Git Vcs root for the releases/hotfixes. Uses the Git [url] and the [authPassword] to set this up.
 *  Calls [gitVcsRoot]
 */
fun Project.releasesHotfixesVcsRoot(
    url: String,
    authPassword: String
) = vcsRoot(
    gitVcsRoot(
        Id.RELEASES_HOTFIXES,
        url,
        authPassword,
        """
    ${Git.getHeadsRef(Git.RELEASES)}
    ${Git.getHeadsRef(Git.HOTFIXES)}
    """.trimIndent(),
        Git.MASTER
    )
)

/**
 * Adds a Git Vcs root for the develop branch. Uses the [url] and the [authPassword]. Calls [gitVcsRoot]
 */
fun Project.developVcsRoot(url: String, authPassword: String) =
    vcsRoot(gitVcsRoot(Git.DEVELOP.capitalize(), url, authPassword, branch = Git.DEVELOP))
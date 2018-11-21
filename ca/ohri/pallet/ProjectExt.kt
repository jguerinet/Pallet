/*
 * Copyright 2015-2018 Ottawa mHealth. All rights reserved.
 */

package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.Project
import jetbrains.buildServer.configs.kotlin.v2018_1.RelativeId
import jetbrains.buildServer.configs.kotlin.v2018_1.VcsRoot
import jetbrains.buildServer.configs.kotlin.v2018_1.vcs.GitVcsRoot

object Git {

    /** Default Git Username */
    const val USERNAME = "mHealthAdmin"

    /** Master branch constant */
    const val MASTER = "master"

    /** Develop branch constant */
    const val DEVELOP = "develop"

    /** Release branch constant */
    const val RELEASE = "release"

    /** Releases formatting branch constant */
    const val RELEASES = "(release-*)"

    /** Hotfixes formatting branch constant */
    const val HOTFIXES = "(hotfix-*)"

    /**
     * Returns the full GitHub Url for the [repoName] in the Ottawa mHealth organization
     */
    fun getUrl(repoName: String) = "https://github.com/Ottawa-mHealth/$repoName"

    /**
     * Returns the String to use to add a pull merge ref to a [branch]. Defaults to any, (*)
     */
    fun addPullMergeRef(branch: String = "(*)") = "+:refs/pull/$branch/merge"

    /**
     * Returns the fully qualified reference to a [branch]
     */
    fun getHeadsRef(branch: String) = "refs/heads/$branch"

    /**
     * Returns the String to use to add a head ref to a [branch]
     */
    fun addHeadsRef(branch: String) = "+:${getHeadsRef(branch)}"
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
 *  The [authUsername] and [authPassword] are used to sign into Git
 */
fun gitVcsRoot(
    id: String,
    url: String,
    authUsername: String,
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
        userName = authUsername
        password = authPassword
    }
    userNameStyle = GitVcsRoot.UserNameStyle.FULL
}
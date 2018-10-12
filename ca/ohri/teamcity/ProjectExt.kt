/*
 * Copyright 2015-2018 Ottawa mHealth. All rights reserved.
 */

package ca.ohri.teamcity

import jetbrains.buildServer.configs.kotlin.v2018_1.Project
import jetbrains.buildServer.configs.kotlin.v2018_1.VcsRoot
import jetbrains.buildServer.configs.kotlin.v2018_1.project
import jetbrains.buildServer.configs.kotlin.v2018_1.projectFeatures.youtrack
import jetbrains.buildServer.configs.kotlin.v2018_1.ui.add
import jetbrains.buildServer.configs.kotlin.v2018_1.vcs.GitVcsRoot

/** Common Git Username for connecting to Git */
val gitUsername = "mHealthAdmin"

/**
 * Initializes an mHealth project with the [init] function. Uses the [youTrackPassword] to sign into
 *  YouTrack (assumes the admin is signing in) for the feature with the [youTrackId]
 */
fun mHealthProject(youTrackId: String, youTrackPassword: String, init: Project.() -> Unit) {
    project {

        init()

        // Always add YouTrack
        features {
            add {
                youtrack {
                    id = youTrackId
                    displayName = "YouTrack"
                    host = "https://ottawamhealth.myjetbrains.com/youtrack"
                    userName = "ottawamhealth"
                    password = youTrackPassword
                    projectExtIds = ""
                    useAutomaticIds = true
                }
            }
        }
    }
}

/**
 * Creates a Git Vcs root using the [id] as the Id and name, the Git [url], and the [branchSpec] to
 *  listen to. It uses the [branch] as the default branch (null if it's the Git default branch).
 *  The [authUsername] and [authPassword] are used to sign into Git
 */
fun gitVcsRoot(id: String, url: String, authUsername: String, authPassword: String,
        branchSpec: String, branch: String? = null):
        VcsRoot = GitVcsRoot {
    id(id)
    name = id
    this.url = url
    if (branch != null) {
        this.branch = "refs/heads/$branch"
    }
    this.branchSpec = branchSpec
    authMethod = password {
        userName = authUsername
        password = authPassword
    }
    userNameStyle = GitVcsRoot.UserNameStyle.FULL
}

/** Returns the full GitHub Url for the [repoName] in the Ottawa mHealth organization */
fun gitHubUrl(repoName: String) = "https://github.com/Ottawa-mHealth/$repoName"
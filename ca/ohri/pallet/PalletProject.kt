package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.Project

class PalletProject(repoName: String, val gitKey: String, init: PalletProject.() -> Unit) : Project() {

    private val url = Git.getUrl(repoName)

    init {
        init()

        // Add the develop Vcs root and the releases/hotfixes Vcs Root
        developVcsRoot()
        releasesHotfixesVcsRoot(Git.MASTER)
    }

    /**
     * Adds a generic Vcs root with the [id], using the [branchSpec] to match and the optional [branch] as the default
     */
    fun vcsRoot(id: String, branchSpec: String? = null, branch: String? = null) =
        vcsRoot(gitVcsRoot(id, url, gitKey, branchSpec, branch))

    /**
     * Adds a Vcs Root that tracks the Develop branch
     */
    fun developVcsRoot() = vcsRoot(Git.DEVELOP.capitalize(), branch = Git.DEVELOP)

    /**
     * Adds the releases/hotfixes Vcs root. Follows the [branch] (defaults ot null)
     */
    fun releasesHotfixesVcsRoot(branch: String? = null) = vcsRoot(gitReleasesHotfixesVcsRoot(url, gitKey, branch))
}
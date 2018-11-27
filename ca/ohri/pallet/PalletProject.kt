package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.Project

class PalletProject(repoName: String, val gitKey: String, init: PalletProject.() -> Unit) : Project() {

    val url = Git.getUrl(repoName)

    init {
        init()
    }

    /**
     * Adds a generic Vcs root with the [id], using the [branchSpec] to match and the optional [branch] as the default
     */
    fun vcsRoot(id: String, branchSpec: String, branch: String? = null) =
        vcsRoot(gitVcsRoot(id, url, gitKey, branchSpec, branch))

    /**
     * Adds the releases/hotfixes Vcs root. Follows the [branch] (defaults ot null)
     */
    fun releasesHotfixesVcsRoot(branch: String? = null) = vcsRoot(gitReleasesHotfixesVcsRoot(url, gitKey, branch))
}
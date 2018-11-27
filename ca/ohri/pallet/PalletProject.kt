package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.Project

class PalletProject(repoName: String, val gitKey: String, init: PalletProject.() -> Unit) : Project() {

    val url = Git.getUrl(repoName)

    init {
        init()
    }

    /**
     * Adds the releases/hotfixes Vcs root. Follows the [branch] (defaults ot null)
     */
    fun releasesHotfixesVcsRoot(branch: String? = null) = vcsRoot(gitReleasesHotfixesVcsRoot(url, gitKey, branch))
}
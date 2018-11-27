package ca.ohri.pallet

object Id {

    /** Relative Id of the Releases/Hotfixes VCS Root */
    const val RELEASES_HOTFIXES = "Releases_Hotfixes"

    /** Relative Id of the Merges VCS Root */
    const val MERGES = "Merges"

    /** Relative Id of the Develop Root */
    const val DEVELOP = "Develop"
}

object Git {

    /** Default Git Username */
    const val USERNAME = "mHealthAdmin"

    /** Master branch constant */
    const val MASTER = "master"

    /** Develop branch constant */
    const val DEVELOP = "develop"

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
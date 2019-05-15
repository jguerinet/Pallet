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

object Id {

    /** Relative Id of the Releases/Hotfixes VCS Root */
    const val RELEASES_HOTFIXES = "Releases_Hotfixes"

    /** Relative Id of the Merges VCS Root */
    const val MERGES = "Merges"

    /** Relative Id of the Develop Root */
    const val DEVELOP = "Develop"
}

object Git {

    /** Master branch constant */
    const val MASTER = "master"

    /** Develop branch constant */
    const val DEVELOP = "develop"

    /** Releases formatting branch constant */
    const val RELEASES = "(release-*)"

    /** Hotfixes formatting branch constant */
    const val HOTFIXES = "(hotfix-*)"

    /**
     * Returns the full GitHub Url for the [repo] (username/repoName)
     */
    fun getUrl(repo: String) = "https://github.com/$repo"

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

package ca.ohri.teamcity

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildFeature
import jetbrains.buildServer.configs.kotlin.v2018_1.BuildFeatures
import jetbrains.buildServer.configs.kotlin.v2018_1.DslContext
import jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures.vcsLabeling

/**
 * Generic BuildFeature extensions
 * @author Julien Guerinet
 * @since 1.0.0
 */

/**
 * Returns a BuildFeature to add a git tag with the version if the build is successful
 */
fun BuildFeatures.tag(): BuildFeature = vcsLabeling {
    vcsRootId = "${DslContext.settingsRoot.id}"
    labelingPattern = "%env.version%"
    successfulOnly = true
}
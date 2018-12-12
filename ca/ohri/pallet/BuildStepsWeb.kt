package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildSteps
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script

/**
 * Web specific BuildStep extensions
 * @author Julien Guerinet
 * @since 3.3.0
 */

/**
 * Returns a build step to extract the current version from the package json
 */
fun BuildSteps.extractWeb(): BuildStep = script {
    name = "Extract version"
    scriptContent = """
        ${setTeamCityVariable(extractPackageJsonVersion())}
    """.trimIndent()
}
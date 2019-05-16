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

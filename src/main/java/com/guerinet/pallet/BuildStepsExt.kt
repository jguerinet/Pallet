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
 * Generic BuildStep extensions
 * @author Julien Guerinet
 * @since 1.0.0
 */

/**
 * Returns a build step to install gems
 */
fun BuildSteps.gem(): BuildStep = script {
    name = "Install Gems"
    scriptContent = "bundle install"
}

/**
 * Returns a build step to deploy the current version using the [lane]
 */
fun BuildSteps.deploy(lane: String): BuildStep = script {
    name = "Deploy"
    scriptContent = "bundle exec fastlane ${lane.toLowerCase()}"
}

/**
 * Returns a build step to commit the changes on Git
 */
fun BuildSteps.commit(): BuildStep = script {
    name = "Commit Version"
    scriptContent = """
        # Add all files
        git add . || exit 1

        # Commit
        git commit -m"Version ${'$'}version" || exit 1

        # Pull
        git pull --rebase || exit 1

        # Push
        git push origin || exit 1
    """.trimIndent()
}

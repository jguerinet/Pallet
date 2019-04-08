/*
 * Copyright 2015-2018 Ottawa mHealth. All rights reserved.
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
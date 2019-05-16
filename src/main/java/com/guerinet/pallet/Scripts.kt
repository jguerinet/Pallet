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

/**
 * Ready-made scripts to be used within a script build step
 * @author Julien Guerinet
 * @since 3.3.0
 */

/**
 * Extracts the Gradle property named [name]
 */
fun extractGradleProperty(name: String) = """`cat gradle.properties | grep "$name" | cut -d'=' -f2`"""

/**
 * Extracts the current version from the package.json
 */
fun extractPackageJsonVersion() = """
    ${'$'}(cat package.json \
        | grep version \
        | head -1 \
        | awk -F: '{ print ${'$'}2 }' \
        | sed 's/[",]//g' \
        | tr -d '[[:space:]]')
""".trimIndent()

/**
 * Changes the package.json version from [oldVersion] to [newVersion]
 */
fun setPackageJsonVersion(oldVersion: String, newVersion: String) = """
    ${replaceText("package.json", "\"version\": \"$oldVersion\",", "\"version\": \"$newVersion\",")}
""".trimIndent()

/**
 * Sets a team city variable [value] for the [name] (defaults to version)
 */
fun setTeamCityVariable(value: String, name: String = "version") =
    """echo "##teamcity[setParameter name='env.$name' value='$value']" """

/**
 * Replaces the [oldText] with the [newText] in the file path
 */
fun replaceText(filePath: String, oldText: String, newText: String) =
    """sed -i'' -e "s/$oldText/$newText/g" $filePath"""

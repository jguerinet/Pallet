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

import jetbrains.buildServer.configs.kotlin.v2018_2.BuildFeatures

/**
 * Android specific BuildFeatures extensions
 * @author Julien Guerinet
 * @since 1.0.0
 */

/**
 * Returns a build feature to parse the Android test report using the [filePath] within the build folder to find the xml
 */
fun BuildFeatures.testReportAndroid(filePath: String) = feature {
    type = "xml-report-plugin"
    param("xmlReportParsing.reportType", "gtest")
    param(
        "xmlReportParsing.reportDirs",
        "+:%teamcity.build.checkoutDir%/app/build/$filePath/**.xml"
    )
}
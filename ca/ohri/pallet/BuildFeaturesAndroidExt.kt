/*
 * Copyright 2015-2018 Ottawa mHealth. All rights reserved.
 */

package ca.ohri.pallet

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildFeatures

/**
 * Android specific BuildFeatures extensions
 * @author Julien Guerinet
 * @since 1.0.0
 */

/**
 * Returns a build feature to parse the Android test report
 */
fun BuildFeatures.testReportAndroid() = feature {
    type = "xml-report-plugin"
    param("xmlReportParsing.reportType", "gtest")
    param("xmlReportParsing.reportDirs", "+:%pallet.build.checkoutDir%/app/build/outputs/androidTest-results/connected/**.xml")
}
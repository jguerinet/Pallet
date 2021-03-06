# Change Log

## Version 5.3.0 (2020-03-02)

-   Updated dependencies
-   Added option to clean the build cache every time for a build
-   Added the `Demo` build type

## Version 5.2.0 (2019-11-19)

-   Updated dependencies
-   Added the `Dev` and `QA` build types

## Version 5.1.1 (2019-09-24)

-   v5.1.0 bug fix

## Version 5.1.0 (2019-09-24)

-   Fix the extraction and incrementing of the build number on iOS

## Version 5.0.3 (2019-05-27)

-   Fixed the `testReportAndroid()` changes

## Version 5.0.2 (2019-05-27)

-   Updated TeamCity to 2019.1
-   Updated deprecated call
-   Changed `testReportAndroid()` to take multiple directories
-   Added step to run fastlane command

## Version 5.0.0 (2019-05-16)

-   Converted project to Gradle
-   Add option to run unit tests and instrumentation tests on Android
-   Set up ktlint
-   Switch Pallet to be reachable via Jitpack as opposed to a Git submodule
-   Move files into conventional folder structure

## Version 4.0.0 (2019-04-08)

-   Prepare library for open source release

## Version 3.2.0 (2018-12-11)

-   Update to TC 2018.2

## Version 3.1.1 (2018-12-11)

-   Fixes to v3.1.0

## Version 3.1.0 (2018-12-11)

-   Updated the new PR feature to use the new 2018.2 format

## Version 3.0.0 (2018-11-27)

-   More opinionated on branches (master/develop setup)
-   Added method to initiate a Pallet project with the right Vcs roots

## Version 2.0.0 (2018-11-21)

-   Add a bunch of Git related functions and static variables
-   Switch to `master` being production, and `develop` being development

## Version 1.1.0 (2018-11-07)

-   Remove the YouTrack initialization code (this is now done at the root project level in TC)

## Version 1.0.6 (2018-11-01)

-   Add a `carthageIos()` build step

## Version 1.0.5 (2018-10-26)

-   Fix bug on the `gitHubPr()` feature

## Version 1.0.4 (2018-10-16)

-   Fix bugs from renaming to pallet.

## Version 1.0.3 (2018-10-12)

-   Remove the `commitPublisher` from the `Key` object

## Version 1.0.2 (2018-10-12)

-   Added the Merges Build Type

## Version 1.0.1 (2018-10-12)

-   Added option to set the order of the build types
-   Changed the GitHub username to a constant

## Version 1.0.0 (2018-10-12)

-   Initial release

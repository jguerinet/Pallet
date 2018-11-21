# Pallet

## Summary
Kotlin library with common code used across all Ottawa mHealth projects for the TeamCity Kotlin DSL 

## Instructions
Dependencies in private repositories are not yet supported in TeamCity. This therefore needs to be added as a git submodule: 

-   `cd .teamcity`
-   `git submodule add -b master https://github.com/Ottawa-mHealth/Pallet.git`

And commit the result. 

You may then use any of Pallet's classes and methods as needed in your Kotlin DSL script. 

## Branches
This repository follows [Git Flow](https://nvie.com/posts/a-successful-git-branching-model/).

-   develop: Current development code.
-   master: Current production code. 
-   release-*: A new release, where * is the new version number. 
-   hotfix-*: A new hotfix for a released version, where * is the new version number. 
-   Any other branches are either feature branches or bugfix branches.

## Contributors
* [Julien Guerinet](https://github.com/jguerinet)

## Version History
See the [Change Log](CHANGELOG.md).

## Copyright
	 Copyright 2018 Ottawa Hospital mHealth Lab. All rights reserved.

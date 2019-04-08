# Pallet

## Summary
Kotlin library with utility functions for the TeamCity Kotlin DSL. 

## Instructions
Dependencies in private repositories are not yet supported in TeamCity. This therefore needs to be added as a git submodule: 

-   `cd .teamcity`
-   `git submodule add -b master https://github.com/jguerinet/Pallet.git`

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

-   [Julien Guerinet](https://github.com/jguerinet)

## Version History
See the [Change Log](CHANGELOG.md).

## Copyright
	Copyright 2018-2019 Julien Guerinet
         
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
     
       http://www.apache.org/licenses/LICENSE-2.0
     
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

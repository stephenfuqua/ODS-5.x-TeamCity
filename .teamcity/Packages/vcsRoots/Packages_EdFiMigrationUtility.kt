// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package Packages.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

object PackagesNetCore31_EdFiMigrationUtility : GitVcsRoot({
    name = "Ed-Fi-MigrationUtility"
    id("PackagesNetCore31_EdFiMigrationUtility")
    url = "https://github.com/%github.organization%/Ed-Fi-MigrationUtility.git"
    branch = "%git.branch.default%"
    branchSpec = "%git.branch.specification%"
})

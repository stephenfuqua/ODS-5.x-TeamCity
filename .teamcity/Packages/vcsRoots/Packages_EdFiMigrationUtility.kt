# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package Packages.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

object Packages_EdFiMigrationUtility : GitVcsRoot({
    name = "Ed-Fi-MigrationUtility"
    url = "git@github.com:Ed-Fi-Alliance-OSS/Ed-Fi-MigrationUtility.git"
    pushUrl = "git@github.com:Ed-Fi-Alliance-OSS/Ed-Fi-MigrationUtility.git"
    branch = "%git.branch.default%"
    branchSpec = "%git.branch.specification%"
    authMethod = uploadedKey {
        uploadedKey = "EdFiBuildAgent"
        passphrase = "zxxb96818c9a505144510759a1451e244861c4e199ad19c58119fec2f7675968a386c862eebc8270999"
    }
})

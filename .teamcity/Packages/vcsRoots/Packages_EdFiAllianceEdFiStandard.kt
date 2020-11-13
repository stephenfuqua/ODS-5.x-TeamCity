// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package Packages.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

object OdsPlatform_Packages_EdFiStandard : GitVcsRoot({
    name = "Ed-Fi-Alliance/Ed-Fi-Standard"
    id("OdsPlatform_Packages_EdFiStandard")
    url = "https://github.com/%github.organization%/Ed-Fi-Standard.git"
    branch = "development"
    branchSpec = "%git.branch.specification%"
})

# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package Packages.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

object Packages_EdFiAllianceEdFiStandard : GitVcsRoot({
    name = "Ed-Fi-Alliance/Ed-Fi-Standard"
    url = "git@github.com:Ed-Fi-Alliance/Ed-Fi-Standard.git"
    branch = "development"
    branchSpec = "%git.branch.specification%"
    userNameStyle = GitVcsRoot.UserNameStyle.NAME
    checkoutSubmodules = GitVcsRoot.CheckoutSubmodules.IGNORE
    serverSideAutoCRLF = true
    useMirrors = false
    authMethod = uploadedKey {
        uploadedKey = "EdFiBuildAgent"
        passphrase = "zxx8ec32f4456a24c4c01db759a77efea148f7feb32b64096a7"
    }
})

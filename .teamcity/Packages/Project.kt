// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package Packages

import Packages.buildTypes.*
import Packages.vcsRoots.*
import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.Project

object Project : Project({
    id("Packages")
    name = "Packages"

    vcsRoot(OdsPlatform_Packages_EdFiStandard)
    vcsRoot(PackagesNetCore31_EdFiMigrationUtility)

    buildType(Packages_EdFiOdsCodeGen)
    buildType(Packages_EdFiOdsExtensionsTpdm)
    buildType(Packages_EdFiOdsPopulatedTemplateTpdmPostgreSQL)
    buildType(PackagesNetCore31_EdFiDbDeploy)
    buildType(Packages_EdFiOdsMinimalTemplatePostgreSQL)
    buildType(Packages_EdFiOdsPopulatedTemplate)
    buildType(Packages_EdFiOdsExtensionsHomograph)
    buildType(Packages_EdFiOdsPopulatedTemplatePostgreSQL)
    buildType(Packages_PostgreSQLBinaries)
    buildType(PackagesNetCore31_EdFiOdsUtilitiesMigration)
    buildType(Packages_EdFiOdsExtensionsSample)
    buildType(Packages_EdFiOdsMinimalTemplate)
    buildType(Packages_EdFiOdsPopulatedTemplateTpdm)
    buildType(Packages_EdFiStandardDescriptors)

    template(Packages_EdFiExtensionsTemplate)
    template(PackagesNetCore31_NetCore31Packages)
    template(Packages_CreateDatabaseTemplate)
})

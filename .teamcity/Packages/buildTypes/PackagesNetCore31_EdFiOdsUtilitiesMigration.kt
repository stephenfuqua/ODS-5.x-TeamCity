# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package Packages.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nuGetPublish

object PackagesNetCore31_EdFiOdsUtilitiesMigration : BuildType({
    templates(PackagesNetCore31_NetCore31Packages)
    name = "EdFi.Ods.Utilities.Migration"

    params {
        param("pathToSolutionFile", """%teamcity.build.checkoutDir%\%MigrationUtilityRoot%\Migration.sln""")
        param("msbuild.exe", "")
        param("PackageId", "EdFi%odsapi.package.suffix%.Ods.Utilities.Migration")
        param("MigrationUtilityRoot", "Ed-Fi-MigrationUtility")
        param("pathToTestFile", """%MigrationUtilityRoot%\**\bin\%msbuild.buildConfiguration%\**\*Tests.dll""")
        param("version.major", "2")
        param("version.minor", "0")
        param("dotnet.pack.parameters", "-p:NoWarn=NU5123 -p:PackageId=%PackageId%")
    }

    vcs {
        root(RelativeId("PackagesNetCore31_EdFiMigrationUtility"), "=> %MigrationUtilityRoot%")
        root(AbsoluteId("OdsPlatform_EdFiOds"), "+:Application/EdFi.Ods.Standard/Artifacts => Ed-Fi-ODS/Application/EdFi.Ods.Standard/Artifacts")
    }

    steps {
        nuGetPublish {
            name = "Publish Prerelease Version"
            id = "RUNNER_195"
            toolPath = "%teamcity.tool.NuGet.CommandLine.DEFAULT%"
            packages = """%nuget.pack.output%\%PackageId%.%version%.nupkg"""
            serverUrl = "%myget.feed%"
            apiKey = "zxx1215e1e3f7b5adec61587ad8f15351ef112824a35fa631d796ec7ab07d6b0f138a88b35b3c76ac9c"
        }
    }
})

# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package Packages.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nuGetPublish

object Packages_EdFiOdsMinimalTemplate : BuildType({
    templates(Packages_CreateDatabaseTemplate)
    name = "EdFi.Ods.Minimal.Template"

    artifactRules = """%nuget.pack.output%\%PackageId%.%version.core%*.nupkg"""

    params {
        param("script.initdev.parameters", "-NoCredentials -NoDeploy")
        param("script.create.template.parameters", """-samplePath "./Ed-Fi-Standard/Descriptors/"  -noExtensions""")
        param("nuget.pack.files", """Ed-Fi-ODS-Implementation\DatabaseTemplate\Database\EdFi.Ods.Minimal.Template.nuspec""")
        param("git.branch.default", "main")
        param("nuget.package.name", "EdFi.Ods.Minimal.Template")
        param("script.create.template", """Ed-Fi-ODS-Implementation\DatabaseTemplate\Modules\create-minimal-template.psm1""")
        param("PackageId", "EdFi%odsapi.package.suffix%.Ods.Minimal.Template")
        param("nuget.pack.properties", """
            id=%PackageId%
            title=%PackageId%
            description=%nuget.package.description%
        """.trimIndent())
        param("nuget.package.description", "EdFi Ods Minimal Template Database")
        param("version.major", "5")
    }

    steps {
        nuGetPublish {
            name = "Publish Prerelease Version"
            id = "RUNNER_199"
            toolPath = "%teamcity.tool.NuGet.CommandLine.DEFAULT%"
            packages = """%nuget.pack.output%\%PackageId%.%version%.nupkg"""
            serverUrl = "%myget.feed%"
            apiKey = "zxx699602564e8236069f2ae7dfb39b25c7"
        }
    }
})

// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package Packages.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.PowerShellStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nuGetPublish
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell

object Packages_EdFiOdsPopulatedTemplatePostgreSQL : BuildType({
    templates(Packages_CreateDatabaseTemplate)
    name = "EdFi.Ods.Populated.Template.PostgreSQL"

    artifactRules = """%nuget.pack.output%\%PackageId%.%version.core%*.nupkg"""

    params {
        param("script.initdev.parameters", "-NoCredentials -NoDeploy -Engine PostgreSQL")
        param("script.create.template.parameters", """-samplePath "./Ed-Fi-Standard/"  -noExtensions -Engine PostgreSQL""")
        param("nuget.pack.files", """Ed-Fi-ODS-Implementation\DatabaseTemplate\Database\EdFi.Ods.Populated.Template.PostgreSQL.nuspec""")
        param("git.branch.default", "main")
        param("nuget.package.name", "EdFi.Ods.Populated.Template.PostgreSQL")
        param("script.create.template", """Ed-Fi-ODS-Implementation\DatabaseTemplate\Modules\create-populated-template.psm1""")
        param("nuget.pack.properties", """
            id=%PackageId%
            title=%PackageId%
            description=%nuget.package.description%
        """.trimIndent())
        param("PackageId", "EdFi%odsapi.package.suffix%.Ods.Populated.Template.PostgreSQL")
        param("nuget.package.description", "EdFi Ods Populated Template Database for PostgreSQL")
        param("version.major", "5")
    }

    steps {
        powerShell {
            name = "Create Database Template (no extensions)"
            id = "RUNNER_196"
            platform = PowerShellStep.Platform.x64
            edition = PowerShellStep.Edition.Desktop
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    Import-Module -Force -Scope Global "%teamcity.build.checkoutDir%\%script.create.template%"
                    Initialize-PopulatedTemplate %script.create.template.parameters%
                """.trimIndent()
            }
            param("jetbrains_powershell_script_file", "Ed-Fi-Common/logistics/scripts/activities/build/create-populated/create-populated.ps1")
        }
        powerShell {
            name = "Remove-EdFiDatabases"
            id = "RUNNER_395"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    ${'$'}ErrorActionPreference = 'Stop'
                    
                    Import-Module "%teamcity.build.checkoutDir%\%script.build.management%"
                    
                    Remove-EdFiDatabases -Force -Engine PostgreSQL
                """.trimIndent()
            }
        }
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

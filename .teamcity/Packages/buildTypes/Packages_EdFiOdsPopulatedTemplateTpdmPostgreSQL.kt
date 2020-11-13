﻿# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package Packages.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.PowerShellStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nuGetPublish
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

object Packages_EdFiOdsPopulatedTemplateTpdmPostgreSQL : BuildType({
    templates(Packages_CreateDatabaseTemplate)
    name = "EdFi.Ods.Populated.Template.TPDM.PostgreSQL"

    artifactRules = """
        %nuget.pack.output%\%PackageId%.%version.core%*.nupkg
        Ed-Fi-ODS\Utilities\DataLoading\EdFi.BulkLoadClient.Console\bin\**\logfile.txt* => result.zip!logs/
        Ed-Fi-ODS-Implementation\Application\EdFi.Ods.Api.IntegrationTestHarness\bin\**\TestHarnessLog.log* => result.zip!logs/
        Ed-Fi-ODS-Implementation\DatabaseTemplate\Database\EdFi.Ods.Populated.Template.TPDM.sql => result.zip!
    """.trimIndent()

    params {
        param("script.initdev.parameters", "-UsePlugins -NoDeploy -Engine PostgreSQL")
        param("script.create.template.parameters", "-Engine PostgreSQL")
        param("nuget.pack.files", "")
        param("nuget.package.name", "")
        param("script.create.template", """Ed-Fi-ODS-Implementation\DatabaseTemplate\Modules\create-tpdm-template.psm1""")
        param("nuget.pack.properties", """
            id=%PackageId%
            title=%PackageId%
            description=%nuget.package.description%
        """.trimIndent())
        param("PackageId", "EdFi%odsapi.package.suffix%.Ods.Populated.Template.TPDM.PostgreSQL")
        param("nuget.package.description", "")
        param("version.major", "5")
    }

    vcs {
        root(AbsoluteId("OdsPlatform_EdFiTpdmExtension"), "+:. => Ed-Fi-TPDM-Extension")
    }

    steps {
        powerShell {
            name = "Create Database Template (no extensions)"
            id = "RUNNER_196"
            platform = PowerShellStep.Platform.x64
            edition = PowerShellStep.Edition.Desktop
            formatStderrAsError = true
            workingDir = "Ed-Fi-ODS-Implementation"
            scriptMode = script {
                content = """
                    Import-Module -Force -Scope Global "%teamcity.build.checkoutDir%\%script.create.template%"
                    Initialize-TPDMTemplate %script.create.template.parameters%
                """.trimIndent()
            }
            param("jetbrains_powershell_script_file", "Ed-Fi-Common/logistics/scripts/activities/build/create-populated/create-populated.ps1")
        }
        nuGetPublish {
            name = "Publish Prerelease Version"
            id = "RUNNER_199"
            enabled = false
            toolPath = "%teamcity.tool.NuGet.CommandLine.DEFAULT%"
            packages = """%nuget.pack.output%\%PackageId%.%version%.nupkg"""
            serverUrl = "%myget.feed%"
            apiKey = "zxx699602564e8236069f2ae7dfb39b25c7"
        }
        stepsOrder = arrayListOf("RUNNER_395", "RUNNER_398", "RUNNER_196", "RUNNER_197", "RUNNER_404", "RUNNER_199")
    }

    triggers {
        vcs {
            id = "vcsTrigger"
            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_CUSTOM
            quietPeriod = 120
            branchFilter = """
                +:<default>
                +:TPDMDEV*
            """.trimIndent()
        }
    }
    
    disableSettings("BUILD_EXT_44", "RUNNER_197", "RUNNER_404")
})

// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package Cloud.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.PowerShellStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nuGetPack
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

object Cloud_CloudOdsApiBuildPackage : BuildType({
    name = "Cloud ODS/API: Build, Package"

    artifactRules = "%nuget.pack.output%/*.nupkg"
    buildNumberPattern = "%version%"
    publishArtifacts = PublishMode.SUCCESSFUL

    params {
        param("nuget.pack.files", "NugetPackages/EdFi.CloudODS.nuspec")
    }

    vcs {
        root(AbsoluteId("OdsPlatform_EdFiOds"), ". => Ed-Fi-ODS")
        root(AbsoluteId("OdsPlatform_EdFiOdsImplementation"), ". => Ed-Fi-ODS-Implementation")

        cleanCheckout = true
        showDependenciesChanges = true
    }

    steps {
        powerShell {
            name = "Remove-EdFiDatabases"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    ${'$'}ErrorActionPreference = 'Stop'
                    
                    Import-Module "%teamcity.build.checkoutDir%\%script.build.management%"
                    
                    Remove-EdFiDatabases -Force
                """.trimIndent()
            }
        }
        powerShell {
            name = "Create Database Backups"
            platform = PowerShellStep.Platform.x64
            edition = PowerShellStep.Edition.Desktop
            formatStderrAsError = true
            scriptMode = script {
                content = ". Ed-Fi-ODS-Implementation/logistics/scripts/activities/build/CloudOds/PrepareDatabasesForExport.ps1 -artifactPath %nuget.pack.output%"
            }
        }
        nuGetPack {
            name = "Create NuGet Packages"
            toolPath = "%teamcity.tool.NuGet.CommandLine.DEFAULT%"
            paths = "%nuget.pack.files%"
            version = "%version%"
            outputDir = "%nuget.pack.output%"
            cleanOutputDir = false
            properties = "%nuget.pack.properties.default%"
            args = "%nuget.pack.parameters%"
        }
    }

    triggers {
        vcs {
            enabled = false
            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_DEFAULT
            branchFilter = "+:<default>"
        }
    }

    dependencies {
        artifacts(AbsoluteId("EdFiBuilds_EdFi20_OdsCi_DataImport_TestInstallerIntegration")) {
            buildRule = lastSuccessful()
            cleanDestination = true
            artifactRules = "EdFi.DataImport.*.zip!** => %nuget.pack.output%/EdFi.DataImport"
        }
        artifacts(AbsoluteId("EdFi_OdsTools_AdminAppForSuite3_BuildBranch")) {
            buildRule = lastSuccessful()
            cleanDestination = true
            artifactRules = """
                +:EdFi%odsapi.package.suffix%.ODS.AdminApp.Web.*.nupkg!** => %nuget.pack.output%/EdFi.ODS.AdminApp.Web
                -:EdFi%odsapi.package.suffix%.ODS.AdminApp.Web.*-*.nupkg
                +:EdFi%odsapi.package.suffix%.ODS.AdminApp.Web.*.nupkg!/Artifacts => Ed-Fi-ODS-AdminApp/Application/EdFi.Ods.AdminApp.Web/Artifacts/
            """.trimIndent()
        }
        artifacts(_Self.buildTypes.OdsApiInitDevUnitTestPackage) {
            buildRule = lastSuccessful()
            cleanDestination = true
            artifactRules = """
                +:EdFi%odsapi.package.suffix%.Ods.SandboxAdmin.*.nupkg!** => %nuget.pack.output%/%odsapi.package.sandboxAdmin%
                +:EdFi%odsapi.package.suffix%.Ods.SwaggerUI.*.nupkg!** => %nuget.pack.output%/%odsapi.package.swaggerUI%
                +:EdFi%odsapi.package.suffix%.Ods.WebApi.*.nupkg!** => %nuget.pack.output%/%odsapi.package.webApi%
                -:EdFi%odsapi.package.suffix%.Ods.WebApi.PreRelease.*.nupkg
            """.trimIndent()
        }
    }
})

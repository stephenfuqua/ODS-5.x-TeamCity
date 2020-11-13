# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package Packages.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.freeDiskSpace
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nuGetPack
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nuGetPublish
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.schedule

object Packages_PostgreSQLBinaries : BuildType({
    name = "PostgreSQL.Binaries"

    artifactRules = """%nuget.pack.output%\%nuget.package.name%*.nupkg"""
    buildNumberPattern = "%version%"

    params {
        param("nuget.pack.files", "%env.TEMP%/%nuget.package.name%/package/package.nuspec")
        param("git.branch.default", "main")
        param("nuget.package.name", "PostgreSQL.Binaries")
        param("script.build.postgres.binaries", """Ed-Fi-ODS-Implementation\logistics\scripts\modules\postgres-binaries\build-postgres-binaries.psm1""")
        param("nuget.pack.properties", """
            id=%nuget.package.name%
            title=%nuget.package.name%
            description=%nuget.package.description%
        """.trimIndent())
        param("nuget.package.description", "%nuget.package.name%")
        param("version.major", "12")
        param("version.minor", "2")
    }

    vcs {
        root(AbsoluteId("OdsPlatform_EdFiOds"), "+:. => Ed-Fi-ODS")
        root(AbsoluteId("OdsPlatform_EdFiOdsImplementation"), "+:. => Ed-Fi-ODS-Implementation")

        cleanCheckout = true
    }

    steps {
        powerShell {
            name = "New-PostgresBinariesPackage"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    Import-Module -Force -Scope Global "%teamcity.build.checkoutDir%\%script.build.postgres.binaries%"
                    New-PostgresBinariesPackage
                """.trimIndent()
            }
        }
        nuGetPack {
            name = "Pack Prerelease version"
            toolPath = "%teamcity.tool.NuGet.CommandLine.DEFAULT%"
            paths = "%nuget.pack.files%"
            version = "%version%"
            outputDir = "%nuget.pack.output%"
            cleanOutputDir = true
            publishPackages = true
            properties = """
                %nuget.pack.properties.default%
                %nuget.pack.properties%
            """.trimIndent()
            args = "%nuget.pack.parameters%"
        }
        nuGetPack {
            name = "Pack Release version"
            toolPath = "%teamcity.tool.NuGet.CommandLine.DEFAULT%"
            paths = "%nuget.pack.files%"
            version = "%version.core%"
            outputDir = "%nuget.pack.output%"
            cleanOutputDir = false
            publishPackages = true
            properties = """
                %nuget.pack.properties.default%
                %nuget.pack.properties%
            """.trimIndent()
            args = "%nuget.pack.parameters%"
        }
        nuGetPublish {
            name = "Publish Prerelease Version"
            toolPath = "%teamcity.tool.NuGet.CommandLine.DEFAULT%"
            packages = """%nuget.pack.output%\%nuget.package.name%.%version%.nupkg"""
            serverUrl = "%myget.feed%"
            apiKey = "zxx699602564e8236069f2ae7dfb39b25c7"
        }
    }

    triggers {
        schedule {
            schedulingPolicy = daily {
                hour = 5
            }
            branchFilter = "+:<default>"
            triggerBuild = always()
            withPendingChangesOnly = false
            enforceCleanCheckout = true
            enforceCleanCheckoutForDependencies = true
        }
    }

    features {
        freeDiskSpace {
            requiredSpace = "%build.feature.freeDiskSpace%"
            failBuild = true
        }
    }
})

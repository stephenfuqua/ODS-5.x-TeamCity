# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package Packages.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.DotnetTestStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.dotnetBuild
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.dotnetPack
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.dotnetRestore
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.dotnetTest
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nuGetPublish
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

object PackagesNetCore31_NetCore31Packages : Template({
    name = "Net Core 3.1 Packages"

    artifactRules = """%nuget.pack.output%\*.nupkg"""
    buildNumberPattern = "%version%"

    params {
        param("pathToSolutionFile", "")
        param("pathToTestFile", "")
        param("version.major", "1")
        param("dotnet.pack.parameters", "-p:NoWarn=NU5123 -p:PackageId=%system.teamcity.buildConfName%%odsapi.package.suffix%")
        param("msbuild.exe", "")
    }

    vcs {
        cleanCheckout = true
    }

    steps {
        dotnetRestore {
            name = "Restore NuGet packages"
            id = "RUNNER_191"
            projects = "%pathToSolutionFile%"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetBuild {
            name = "Build Solution"
            id = "RUNNER_189"
            projects = "%pathToSolutionFile%"
            configuration = "%msbuild.buildConfiguration%"
            versionSuffix = "%version%"
            args = "--no-restore"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetTest {
            name = "Run Tests"
            id = "RUNNER_319"
            projects = "%pathToSolutionFile%"
            configuration = "%msbuild.buildConfiguration%"
            skipBuild = true
            args = "--no-build --filter TestCategory!=LocalOnly"
            logging = DotnetTestStep.Verbosity.Normal
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetPack {
            name = "Pack Prerelease version"
            id = "RUNNER_193"
            projects = "%pathToSolutionFile%"
            configuration = "%msbuild.buildConfiguration%"
            outputDir = "%nuget.pack.output%"
            skipBuild = true
            args = "-p:PackageVersion=%version% %dotnet.pack.parameters%"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetPack {
            name = "Pack Release version"
            id = "RUNNER_194"
            projects = "%pathToSolutionFile%"
            configuration = "%msbuild.buildConfiguration%"
            outputDir = "%nuget.pack.output%"
            skipBuild = true
            args = "-p:PackageVersion=%version.core% %dotnet.pack.parameters%"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        nuGetPublish {
            name = "Publish Prerelease Version"
            id = "RUNNER_195"
            toolPath = "%teamcity.tool.NuGet.CommandLine.DEFAULT%"
            packages = """%nuget.pack.output%\*.%version%.nupkg"""
            serverUrl = "%myget.feed%"
            apiKey = "zxx1215e1e3f7b5adec61587ad8f15351ef112824a35fa631d796ec7ab07d6b0f138a88b35b3c76ac9c"
        }
    }

    triggers {
        vcs {
            id = "vcsTrigger"
            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_DEFAULT
            triggerRules = "+:**"
            branchFilter = """
                +:*
                -:*-v2
            """.trimIndent()
        }
    }

    features {
        commitStatusPublisher {
            id = "BUILD_EXT_43"
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "zxxd7502a9db2ed52447d5208ad7b0fac68e7ad2247126718b7"
                }
            }
        }
    }
})

// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package Packages.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.DotnetPackStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.dotnetPack
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nuGetPublish

object Packages_EdFiOdsCodeGen : BuildType({
    templates(PackagesNetCore31_NetCore31Packages)
    name = "EdFi.Ods.CodeGen"

    artifactRules = """%nuget.pack.output%\%PackageId%.%version.core%*.nupkg"""

    params {
        param("pathToUtilityProjectFile", """Utilities\CodeGeneration\EdFi.Ods.CodeGen.Console\EdFi.Ods.CodeGen.Console.csproj""")
        param("pathToSolutionFile", """Utilities\CodeGeneration\EdFi.Ods.CodeGen\EdFi.Ods.CodeGen.csproj""")
        param("msbuild.exe", "")
        param("PackageId", "EdFi%odsapi.package.suffix%.Ods.CodeGen")
        param("pathToTestFile", """Utilities\CodeGeneration\**\bin\%msbuild.buildConfiguration%\**\*Tests.dll""")
        param("version.major", "5")
        param("dotnet.pack.parameters", "-p:NoWarn=NU5123 -p:PackageId=%PackageId%")
    }

    vcs {
        root(AbsoluteId("OdsPlatform_EdFiOds"))
    }

    steps {
        dotnetPack {
            name = "Pack Prerelease version"
            id = "RUNNER_193"
            projects = "%pathToSolutionFile%"
            configuration = "%msbuild.buildConfiguration%"
            outputDir = "%nuget.pack.output%"
            skipBuild = true
            args = "-p:PackageVersion=%version% %dotnet.pack.parameters%"
            logging = DotnetPackStep.Verbosity.Normal
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
            logging = DotnetPackStep.Verbosity.Normal
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
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

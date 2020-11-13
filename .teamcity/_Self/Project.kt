# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package _Self

import _Self.buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.Project
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.buildReportTab

object Project : Project({
    description = "Primary ODS/API builds"

    buildType(Deploy_And_Smoke_Test_YearSpecific)
    buildType(OdsApiSmokeTestStaging)
    buildType(OdsApiInitDevUnitTestPackage)
    buildType(OdsApiDeployToStaging)
    buildType(Deploy_And_SmokeTest_SharedInstance)
    buildType(OdsApiInitDevIntegrationTest)
    buildType(OdsApiDeployLandingPageToStaging)
    buildType(OdsApiPublishToMyGet)

    template(OdsApi_Deploy_And_SmokeTest_SharedInstanceOrYearSpecific)

    params {
        param("script.initdev.parameters", "-UsePlugins")
        param("odsapi.package.webApi", "EdFi.Ods.WebApi")
        param("nuget.pack.properties.copyright", "Copyright Â©Ed-Fi Alliance, LLC. 2020")
        param("octopus.release.channel", "v%version.core%")
        param("nuget.pack.properties.owners", "Ed-Fi Alliance")
        param("nuget.pack.properties.authors", "Ed-Fi Alliance")
        param("odsapi.package.sandboxAdmin", "EdFi.Ods.SandboxAdmin")
        param("odsapi.package.swaggerUI", "EdFi.Ods.SwaggerUI")
        param("odsapi.package.smokeTest", "EdFi%odsapi.package.suffix%.SmokeTest.Console")
        param("script.initdev", """Ed-Fi-ODS-Implementation\Initialize-PowershellForDevelopment.ps1""")
        param("odsapi.package.sdk", "EdFi%odsapi.package.suffix%.OdsApi.Sdk")
        param("version.prerelease.prefix", "b")
        param("nuget.pack.output", "NugetPackages")
        param("nuget.pack.properties.default", """
            configuration=%msbuild.buildConfiguration%
            authors=%nuget.pack.properties.authors%
            owners=%nuget.pack.properties.owners%
            copyright=%nuget.pack.properties.copyright%
        """.trimIndent())
        param("version.patch", "0")
        param("nuget.pack.parameters", "-NoPackageAnalysis -NoDefaultExcludes")
        param("version.major", "5")
        param("odsapi.package.databases", "EdFi.RestApi.Databases")
        param("version.minor", "2")
        param("script.build.management", """Ed-Fi-ODS-Implementation\logistics\scripts\modules\build-management.psm1""")
        param("version.suite", "3")
        param("datastandard.version", "v3.2")
        param("odsapi.package.suffix", ".Suite%version.suite%")
    }

    features {
        buildReportTab {
            id = "PROJECT_EXT_18"
            title = "NUnit"
            startPage = "TestResult.xml"
        }
    }
    buildTypesOrder = arrayListOf(OdsApiInitDevUnitTestPackage, OdsApiInitDevIntegrationTest, OdsApiDeployToStaging, OdsApiSmokeTestStaging, Deploy_And_Smoke_Test_YearSpecific, Deploy_And_SmokeTest_SharedInstance, OdsApiDeployLandingPageToStaging, OdsApiPublishToMyGet)
    subProjectsOrder = arrayListOf(RelativeId("PostgreSQL"), RelativeId("Cloud"), RelativeId("Packages"), RelativeId("MetaEdIntegration"))

    subProject(MetaEdIntegration.Project)
    subProject(Cloud.Project)
    subProject(Packages.Project)
    subProject(PostgreSQL.Project)
})

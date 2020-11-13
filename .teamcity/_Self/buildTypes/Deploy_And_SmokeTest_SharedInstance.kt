# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger

object Deploy_And_SmokeTest_SharedInstance : BuildType({
    templates(OdsApi_Deploy_And_SmokeTest_SharedInstanceOrYearSpecific)
    name = "ODS/API: Deploy and Smoke Test Shared Instance"
    description = "Deploys the ODS/API in Shared Instance mode on the Staging server and runs the Smoke Test utility"

    buildNumberPattern = "${OdsApiInitDevUnitTestPackage.depParamRefs.buildNumber}"

    params {
        param("odsapi.package.sdk", "EdFi%odsapi.package.suffix%.OdsApi.Sdk")
        param("odsapi.package.webApi", "EdFi.Ods.WebApi")
        param("odsapi.smokeTestExecutable", "EdFi.SmokeTest.Console")
        param("octopus.project.name", "Ed-Fi ODS Shared Instance (SQL Server)")
        param("odsapi.package.databases", "EdFi%odsapi.package.suffix%.RestApi.Databases")
        param("environment.webApi.startupType", "SharedInstance")
        param("odsapi.package.smokeTest", "EdFi%odsapi.package.suffix%.SmokeTest.Console")
    }

    steps {
        powerShell {
            name = "Run Smoketest"
            id = "RUNNER_389"
            scriptMode = script {
                content = """
                    ${'$'}ErrorActionPreference = 'Stop'
                    Import-Module -Force "%teamcity.build.checkoutDir%\%script.module.loadtools%"
                    
                    ${'$'}config = @{
                        smokeTestExecutable = ".\%odsapi.package.smokeTest%\tools\%odsapi.smokeTestExecutable%.exe"
                        smokeTestDll = ".\%odsapi.package.sdk%\lib\%odsapi.package.sdk%.dll"
                        apiKey = "%smokeTest.credential.key%"
                        apiSecret = "%smokeTest.credential.secret%"
                        apiUrlMetadata = "%environment.webApi.metadataUrl%"
                        apiUrlOAuth = "%smokeTest.oauthUrl%" 
                        apiUrlData = "%smokeTest.apiUrl%"
                        apiNamespaceUri = "http://edfi.org"
                        apiYear = "%environment.webApi.odsYear%"
                        testSets = @("NonDestructiveApi")
                    }
                    Write-Host ${'$'}config
                    Invoke-SmokeTestClient -config ${'$'}config
                    
                    Test-Error
                """.trimIndent()
            }
        }
    }

    triggers {
        finishBuildTrigger {
            id = "TRIGGER_41"
            buildType = "${Deploy_And_Smoke_Test_YearSpecific.id}"
            successfulOnly = true
            branchFilter = "+:*"
        }
    }

    dependencies {
        snapshot(Deploy_And_Smoke_Test_YearSpecific) {
            onDependencyFailure = FailureAction.CANCEL
            onDependencyCancel = FailureAction.CANCEL
        }
    }
})

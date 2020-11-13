// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger

object OdsApiSmokeTestStaging : BuildType({
    templates(RelativeId("OdsApiSmokeTestStagingTemplate"))
    name = "ODS/API: Smoke Test Staging"

    buildNumberPattern = "${OdsApiDeployToStaging.depParamRefs.buildNumber}"

    params {
        param("odsapi.dllname.sdk", "EdFi.OdsApi.Sdk")
        param("odsapi.package.sdk", "EdFi%odsapi.package.suffix%.OdsApi.TestSdk")
        param("odsapi.smokeTestExecutable", "EdFi.SmokeTest.Console")
        param("git.branch.default", "main")
        param("odsapi.package.databases", "EdFi%odsapi.package.suffix%.RestApi.Databases")
        param("odsapi.package.smokeTest", "EdFi%odsapi.package.suffix%.SmokeTest.Console")
    }

    steps {
        powerShell {
            name = "Invoke-SmokeTest"
            id = "RUNNER_406"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    ${'$'}ErrorActionPreference = 'Stop'
                    
                    Import-Module -Force "%teamcity.build.checkoutDir%\%script.module.loadtools%"
                    
                    Clear-Error
                    
                    ${'$'}config = @{
                        smokeTestExecutable = ".\%odsapi.package.smokeTest%\tools\%odsapi.smokeTestExecutable%.exe"
                        smokeTestDll = ".\%odsapi.package.sdk%\lib\%odsapi.dllname.sdk%.dll"
                        apiKey = "smoke"
                        apiSecret = "smokeSecret"
                        apiUrlMetadata = "%environment.staging.metadataUrl%"
                        apiUrlOAuth = "https://api-stage.ed-fi.org/%octopus.release.channel%/api/"
                        apiUrlData = "https://api-stage.ed-fi.org/%octopus.release.channel%/api/data/v3"
                        apiNamespaceUri = "http://edfi.org"
                        testSets = @("NonDestructiveApi", "NonDestructiveSdk")
                    }
                    
                    Write-Host ${'$'}config
                    
                    Invoke-SmokeTestClient -config ${'$'}config
                """.trimIndent()
            }
        }
    }

    triggers {
        finishBuildTrigger {
            id = "TRIGGER_7"
            buildType = "${OdsApiDeployToStaging.id}"
            successfulOnly = true
            branchFilter = "+:*"
        }
    }

    dependencies {
        dependency(AbsoluteId("EdFiBuilds_EdFi20_OdsCi_V3_NuGetPackages_EdFiOdsApiTestSdkV3TestSdkWithSampleExtensionsForSmokeTest")) {
            snapshot {
                onDependencyFailure = FailureAction.CANCEL
                onDependencyCancel = FailureAction.CANCEL
            }

            artifacts {
                id = "ARTIFACT_DEPENDENCY_38"
                artifactRules = "%odsapi.package.sdk%.*.nupkg!** => %odsapi.package.sdk%"
            }
        }
        snapshot(OdsApiDeployToStaging) {
        }
        snapshot(OdsApiInitDevUnitTestPackage) {
        }
    }
})

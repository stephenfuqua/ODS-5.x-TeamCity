// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package PostgreSQL.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell

object PostgreSQL_OdsApiInitDevIntegrationTest : BuildType({
    templates(AbsoluteId("OdsPlatform_OdsApiInitDevUnitTestPackageTemplate"))
    name = "ODS/API: InitDev, Integration Test"

    params {
        param("script.initdev.parameters", "-UsePlugins -Engine PostgreSQL")
        param("odsapi.package.sdk", "EdFi%odsapi.package.suffix%.OdsApi.Sdk")
        param("odsapi.smokeTestExecutable", "EdFi.SmokeTest.Console")
        param("git.branch.default", "main")
        param("odsapi.package.smokeTest", "EdFi%odsapi.package.suffix%.SmokeTest.Console")
    }

    steps {
        powerShell {
            name = "Invoke-PostmanIntegrationTests"
            id = "RUNNER_403"
            formatStderrAsError = true
            workingDir = "Ed-Fi-ODS-Implementation"
            scriptMode = script {
                content = """
                    ${'$'}ErrorActionPreference = 'Stop'
                    
                    . "%teamcity.build.checkoutDir%\%script.initdev%"
                    
                    Invoke-PostmanIntegrationTests
                """.trimIndent()
            }
        }
        powerShell {
            name = "Remove-EdFiDatabases"
            id = "RUNNER_41"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    ${'$'}ErrorActionPreference = 'Stop'
                    
                    Import-Module "%teamcity.build.checkoutDir%\%script.build.management%"
                    
                    Remove-EdFiDatabases -Force
                    Remove-EdFiDatabases -Force -Engine PostgreSQL
                """.trimIndent()
            }
        }
        powerShell {
            name = "Run Smoke Tests"
            id = "RUNNER_362"
            workingDir = "Ed-Fi-ODS-Implementation"
            scriptMode = script {
                content = """
                    ${'$'}ErrorActionPreference = 'Stop'
                    
                    ${'$'}params = @{
                        smokeTestExe = ".\%odsapi.package.smokeTest%\tools\%odsapi.smokeTestExecutable%.exe"
                        smokeTestDll = ".\%odsapi.package.sdk%\lib\%odsapi.package.sdk%.dll"
                        key = "smoke"
                        secret = "smokeSecret"
                        metadataUrl = "http://localhost:8765/metadata"
                        apiUrl = "http://localhost:8765"
                        apiDataUrl = "http://localhost:8765/data/v3"
                        namespaceUri = "http://edfi.org"
                        testSets = @("NonDestructiveApi")
                    }
                    
                    & "%teamcity.build.checkoutDir%\%script.run.smoketests%" @params
                    
                    Test-Error
                """.trimIndent()
            }
            param("jetbrains_powershell_script_file", """logistics\scripts\run-smoke-tests.ps1""")
        }
    }
    
    disableSettings("RUNNER_332", "RUNNER_392", "RUNNER_417", "RUNNER_438", "RUNNER_89", "RUNNER_90", "RUNNER_93")
})

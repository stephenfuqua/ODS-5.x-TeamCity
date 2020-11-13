// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.FileContentReplacer
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.replaceContent
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2019_2.failureConditions.BuildFailureOnMetric
import jetbrains.buildServer.configs.kotlin.v2019_2.failureConditions.failOnMetricChange

object OdsApiInitDevUnitTestPackage : BuildType({
    templates(AbsoluteId("OdsPlatform_OdsApiInitDevUnitTestPackageTemplate"))
    name = "ODS/API: InitDev, Unit Test, Package"

    publishArtifacts = PublishMode.SUCCESSFUL

    params {
        param("git.branch.default", "main")
        param("odsapi.package.nugetdatabasename", "EdFi%odsapi.package.suffix%.RestApi.Databases")
    }

    vcs {
        branchFilter = "+:*"
    }

    steps {
        powerShell {
            name = "Create EdFi.RestApi.Databases Nuspec File"
            id = "RUNNER_89"
            formatStderrAsError = true
            scriptMode = file {
                path = "%script.prep.package%"
            }
            param("jetbrains_powershell_scriptArguments", "-packageName %odsapi.package.nugetdatabasename%")
        }
        powerShell {
            name = "Initialize-DevelopmentEnvironment"
            id = "RUNNER_355"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    ${'$'}ErrorActionPreference = 'Stop'
                    
                    . "%teamcity.build.checkoutDir%\%script.initdev%"
                    Initialize-DevelopmentEnvironment %script.initdev.parameters%
                """.trimIndent()
            }
        }
        stepsOrder = arrayListOf("RUNNER_41", "RUNNER_355", "RUNNER_332", "RUNNER_392", "RUNNER_417", "RUNNER_421", "RUNNER_403", "RUNNER_362", "RUNNER_89", "RUNNER_418", "RUNNER_90")
    }

    failureConditions {
        failOnMetricChange {
            id = "BUILD_EXT_69"
            metric = BuildFailureOnMetric.MetricType.TEST_FAILED_COUNT
            threshold = 0
            units = BuildFailureOnMetric.MetricUnit.DEFAULT_UNIT
            comparison = BuildFailureOnMetric.MetricComparison.MORE
            compareTo = value()
        }
    }

    features {
        replaceContent {
            id = "BUILD_EXT_60"
            fileRules = "**/*.nuspec"
            pattern = """(?<=<id>)(.*?)(EdFi)(?=\b.*</id>)"""
            caseSensitivePattern = false
            replacement = "%odsapi.package.suitenumber%"
            encoding = FileContentReplacer.FileEncoding.UTF_8
            customEncodingName = "UTF-8"
        }
    }
    
    disableSettings("ARTIFACT_DEPENDENCY_39", "ARTIFACT_DEPENDENCY_41", "RUNNER_154", "RUNNER_362", "RUNNER_403", "RUNNER_417", "RUNNER_421", "RUNNER_93")
})

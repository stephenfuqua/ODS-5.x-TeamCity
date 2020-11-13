// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nuGetPublish
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger

object OdsApiPublishToMyGet : BuildType({
    name = "ODS/API: Publish to MyGet"

    buildNumberPattern = "${OdsApiDeployToStaging.depParamRefs.buildNumber}"

    params {
        param("version", "${OdsApiInitDevUnitTestPackage.depParamRefs["version"]}")
    }

    vcs {
        showDependenciesChanges = true
    }

    steps {
        nuGetPublish {
            name = "Publish to MyGet"
            toolPath = "%teamcity.tool.NuGet.CommandLine.DEFAULT%"
            packages = "*.nupkg"
            serverUrl = "%myget.feed%"
            apiKey = "zxx1215e1e3f7b5adec61587ad8f15351ef112824a35fa631d796ec7ab07d6b0f138a88b35b3c76ac9c"
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${OdsApiSmokeTestStaging.id}"
            successfulOnly = true
            branchFilter = """
                +:<default>
                +:master-v3
            """.trimIndent()
        }
    }

    dependencies {
        snapshot(Deploy_And_SmokeTest_SharedInstance) {
            onDependencyFailure = FailureAction.CANCEL
            onDependencyCancel = FailureAction.CANCEL
        }
        snapshot(Deploy_And_Smoke_Test_YearSpecific) {
            onDependencyFailure = FailureAction.CANCEL
            onDependencyCancel = FailureAction.CANCEL
        }
        snapshot(OdsApiDeployToStaging) {
            onDependencyFailure = FailureAction.CANCEL
            onDependencyCancel = FailureAction.CANCEL
        }
        snapshot(OdsApiInitDevIntegrationTest) {
            onDependencyFailure = FailureAction.CANCEL
            onDependencyCancel = FailureAction.CANCEL
        }
        dependency(OdsApiInitDevUnitTestPackage) {
            snapshot {
                onDependencyFailure = FailureAction.CANCEL
                onDependencyCancel = FailureAction.CANCEL
            }

            artifacts {
                cleanDestination = true
                artifactRules = "*.nupkg => ."
            }
        }
        snapshot(OdsApiSmokeTestStaging) {
            onDependencyFailure = FailureAction.CANCEL
            onDependencyCancel = FailureAction.CANCEL
        }
    }
})

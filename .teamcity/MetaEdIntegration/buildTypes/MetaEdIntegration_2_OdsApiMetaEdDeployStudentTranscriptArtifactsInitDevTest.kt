// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package MetaEdIntegration.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.schedule

object MetaEdIntegration_2_OdsApiMetaEdDeployStudentTranscriptArtifactsInitDevTest : BuildType({
    templates(AbsoluteId("OdsPlatform_OdsApiInitDevUnitTestPackageTemplate"))
    name = "ODS/API+MetaEd: Deploy Student Transcript Artifacts, InitDev, Test"

    params {
        param("metaed.deploy", """run metaed:deploy -- --source %system.teamcity.build.checkoutDir%\%datastandard.source% %system.teamcity.build.checkoutDir%\%extension.source% --target %system.teamcity.build.checkoutDir% --core --defaultPluginTechVersion %metaed.technologyVersion% --projectNames Ed-Fi Homograph""")
        param("odsapi.package.webApi", "EdFi.Ods.WebApi")
        param("version.major", "5")
        param("extension.source", """Ed-Fi-Ods\Samples\Extensions\StudentTranscript\StudentTranscriptMetaEd""")
    }

    vcs {
        root(_Self.vcsRoots.OdsPlatform_EdFiExtensions, "+:Extensions/EdFi.Ods.Extensions.Homograph=>Ed-Fi-ODS-Implementation/Application/EdFi.Ods.Extensions.Homograph")
    }

    steps {
        powerShell {
            name = "Select-NodeVersion"
            id = "RUNNER_410"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    ${'$'}ErrorActionPreference = 'Stop'
                    
                    Import-Module "%teamcity.build.checkoutDir%\%script.build.management%"
                    
                    ${'$'}params = @{
                        version= "%node.version%"
                    }
                    Select-NodeVersion @params
                """.trimIndent()
            }
        }
        step {
            name = "run metaed:deploy"
            id = "RUNNER_412"
            type = "jonnyzzz.npm"
            param("teamcity.build.workingDir", "%metaed.source%")
            param("npm_commands", "%metaed.deploy%")
        }
        stepsOrder = arrayListOf("RUNNER_410", "RUNNER_412", "RUNNER_41", "RUNNER_355", "RUNNER_93", "RUNNER_417", "RUNNER_421", "RUNNER_403", "RUNNER_362", "RUNNER_89", "RUNNER_90")
    }

    triggers {
        schedule {
            id = "TRIGGER_36"
            schedulingPolicy = daily {
                hour = 5
            }
            branchFilter = "+:<default>"
            triggerBuild = always()
            enforceCleanCheckout = true
            param("revisionRuleBuildBranch", "<default>")
        }
    }

    dependencies {
        artifacts(AbsoluteId("MetaEd_MetaEdJsCi")) {
            id = "ARTIFACT_DEPENDENCY_48"
            buildRule = lastSuccessful()
            artifactRules = "%metaed.source%.zip!** => %metaed.source%"
        }
    }
    
    disableSettings("RUNNER_89", "RUNNER_90", "vcsTrigger")
})

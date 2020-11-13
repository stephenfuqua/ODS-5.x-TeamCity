# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package MetaEdIntegration.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.schedule

object MetaEdIntegration_OdsApiMetaEdDeployExtensionArtifactsInitDevTest : BuildType({
    templates(AbsoluteId("OdsPlatform_OdsApiInitDevUnitTestPackageTemplate"))
    name = "ODS/API+MetaEd: Deploy Extension Artifacts, InitDev, Test"

    artifactRules = """
        %nuget.pack.output%/** => .
        %metaed.deploy.output%
    """.trimIndent()

    params {
        param("metaed.deploy", """run metaed:deploy -- --source %system.teamcity.build.checkoutDir%\%datastandard.source% %extension.source% --target %system.teamcity.build.checkoutDir% --defaultPluginTechVersion %metaed.technologyVersion% --suppressDelete""")
        param("metaed.output", """%system.teamcity.build.checkoutDir%\Ed-Fi-ODS-Implementation\Extensions\TPDM\TPDMMedataEd\MetaEdOutput\""")
        param("metaed.deploy.output", """
            Ed-Fi-ODS-Implementation\Application\EdFi.Ods.Extensions.Sample\Artifacts\** => ExtensionMetaEdDeploy.zip!Ed-Fi-ODS-Implementation\Application\EdFi.Ods.Extensions.Sample\Artifacts\
            Ed-Fi-ODS-Implementation\Application\EdFi.Ods.Extensions.Homograph\Artifacts\** => ExtensionMetaEdDeploy.zip!Ed-Fi-ODS-Implementation\Application\EdFi.Ods.Extensions.Homograph\Artifacts\
            Ed-Fi-ODS-Implementation\Application\EdFi.Ods.Extensions.TPDM\Artifacts\** => ExtensionMetaEdDeploy.zip!Ed-Fi-ODS-Implementation\Application\EdFi.Ods.Extensions.TPDM\Artifacts\
        """.trimIndent())
        param("odsapi.package.webApi", "EdFi.Ods.WebApi")
        param("extension.project", "EdFi.Ods.Extensions.TPDM")
        param("version.major", "5")
        param("extension.source", """%system.teamcity.build.checkoutDir%\Ed-Fi-Extensions\Extensions\EdFi.Ods.Extensions.Homograph %system.teamcity.build.checkoutDir%\Ed-Fi-Extensions\Extensions\EdFi.Ods.Extensions.Sample %system.teamcity.build.checkoutDir%\Ed-Fi-Extensions\Extensions\EdFi.Ods.Extensions.TPDM""")
    }

    vcs {
        root(AbsoluteId("OdsPlatform_EdFiExtensions"), "=> Ed-Fi-Extensions")
    }

    steps {
        step {
            name = "run metaed:deploy"
            id = "RUNNER_412"
            type = "jonnyzzz.npm"
            param("teamcity.build.workingDir", "%metaed.source%")
            param("npm_commands", "%metaed.deploy%")
        }
        stepsOrder = arrayListOf("RUNNER_421", "RUNNER_412", "RUNNER_41", "RUNNER_355", "RUNNER_93", "RUNNER_417", "RUNNER_403", "RUNNER_362", "RUNNER_89", "RUNNER_90")
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

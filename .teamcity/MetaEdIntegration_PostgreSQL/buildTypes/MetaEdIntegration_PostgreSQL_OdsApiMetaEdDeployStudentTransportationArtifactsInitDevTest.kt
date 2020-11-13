# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package MetaEdIntegration_PostgreSQL.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.schedule

object MetaEdIntegration_PostgreSQL_OdsApiMetaEdDeployStudentTransportationArtifactsInitDevTest : BuildType({
    templates(AbsoluteId("OdsPlatform_OdsApiInitDevUnitTestPackageTemplate"))
    name = "ODS/API+MetaEd: Deploy Student Transportation Artifacts, InitDev, Test"

    params {
        param("metaed.deploy", """run metaed:deploy -- --source %system.teamcity.build.checkoutDir%\%datastandard.source% %system.teamcity.build.checkoutDir%\%extension.source% --target %system.teamcity.build.checkoutDir% --core --defaultPluginTechVersion %metaed.technologyVersion% --projectNames Ed-Fi Homograph""")
        param("version.major", "5")
        param("extension.source", """Ed-Fi-Ods\Samples\Extensions\StudentTransportation\StudentTransportationMetaEd""")
    }

    steps {
        step {
            name = "run metaed:deploy"
            id = "RUNNER_413"
            type = "jonnyzzz.npm"
            param("teamcity.build.workingDir", "%metaed.source%")
            param("npm_commands", "%metaed.deploy%")
        }
        stepsOrder = arrayListOf("RUNNER_421", "RUNNER_413", "RUNNER_41", "RUNNER_355", "RUNNER_93", "RUNNER_417", "RUNNER_403", "RUNNER_362", "RUNNER_89", "RUNNER_90")
    }

    triggers {
        schedule {
            id = "TRIGGER_37"
            schedulingPolicy = daily {
                hour = 4
            }
            branchFilter = "+:<default>"
            triggerBuild = always()
            enforceCleanCheckout = true
            param("revisionRuleBuildBranch", "<default>")
        }
    }

    dependencies {
        artifacts(AbsoluteId("MetaEd_MetaEdJsCi")) {
            id = "ARTIFACT_DEPENDENCY_49"
            buildRule = lastSuccessful()
            artifactRules = "%metaed.source%.zip!** => %metaed.source%"
        }
    }
    
    disableSettings("RUNNER_332", "RUNNER_392", "RUNNER_417", "RUNNER_89", "RUNNER_90", "RUNNER_93", "vcsTrigger")
})

# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger

object OdsApiDeployToStaging : BuildType({
    templates(AbsoluteId("OdsPlatform_OdsApiDeployToStagingTemplate"))
    name = "ODS/API: Deploy to Staging"

    buildNumberPattern = "${OdsApiInitDevUnitTestPackage.depParamRefs.buildNumber}"

    params {
        param("odsapi.package.sandboxAdmin", "EdFi.Ods.SandboxAdmin.Web")
        param("odsapi.package.swaggerUI", "EdFi.Ods.SwaggerUI")
        param("git.branch.default", "main")
        param("version", "${OdsApiInitDevUnitTestPackage.depParamRefs["version"]}")
        param("odsapi.package.databases", "EdFi%odsapi.package.suffix%.RestApi.Databases")
    }

    triggers {
        finishBuildTrigger {
            id = "TRIGGER_14"
            buildType = "${OdsApiInitDevUnitTestPackage.id}"
            successfulOnly = true
            branchFilter = """
                +:<default>
                +:master-v3
            """.trimIndent()
        }
    }

    dependencies {
        snapshot(OdsApiInitDevUnitTestPackage) {
        }
    }
})

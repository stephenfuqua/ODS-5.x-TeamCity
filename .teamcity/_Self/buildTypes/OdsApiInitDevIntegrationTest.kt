// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*

object OdsApiInitDevIntegrationTest : BuildType({
    templates(AbsoluteId("OdsPlatform_OdsApiInitDevUnitTestPackageTemplate"))
    name = "ODS/API: InitDev, Integration Test"

    params {
        param("git.branch.default", "main")
    }
    
    disableSettings("RUNNER_392", "RUNNER_417", "RUNNER_438", "RUNNER_456", "RUNNER_89", "RUNNER_90", "RUNNER_93")
})

// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package MetaEdIntegration_PostgreSQL

import MetaEdIntegration_PostgreSQL.buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.Project

object Project : Project({
    id("MetaEdIntegration_PostgreSQL")
    name = "PostgreSQL"

    buildType(MetaEdIntegration_PostgreSQL_OdsApiMetaEdDeployExtensionArtifactsInitDevTest)
    buildType(MetaEdIntegration_PostgreSQL_OdsApiMetaEdDeployStudentTranscriptArtifactsInitDevTest)
    buildType(MetaEdIntegration_PostgreSQL_OdsApiMetaEdDeployStudentTransportationArtifactsInitDevTest)
    buildType(MetaEdIntegration_PostgreSQL_OdsApiMetaEdDeployDataStandardArtifactsInitDevTest)

    params {
        param("script.initdev.parameters", "-NoCredentials -Engine PostgreSQL")
    }
})

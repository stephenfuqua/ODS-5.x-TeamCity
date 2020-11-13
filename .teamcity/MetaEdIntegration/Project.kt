// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements..
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information..

package MetaEdIntegration

import MetaEdIntegration.buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.Project

object Project : Project({
    id("MetaEdIntegration")
    name = "MetaEd Integration"
    description = "Utillizes MetaEd to Deploy the Latest Data Standard to the ODS/API for Testing"

    buildType(MetaEdIntegration_OdsApiMetaEdDeployExtensionArtifactsInitDevTest)
    buildType(MetaEdIntegration_2_OdsApiMetaEdDeployStudentTranscriptArtifactsInitDevTest)
    buildType(MetaEdIntegration_2_OdsApiMetaEdDeployStudentTransportationArtifactsInitDevTest)
    buildType(MetaEdIntegration_2_OdsApiMetaEdDeployDataStandardArtifactsInitDevTest)

    params {
        param("metaed.technologyVersion", "%version.core%")
        param("metaed.source", "MetaEd-js")
        param("datastandard.source", """%metaed.source%\node_modules\ed-fi-model-%datastandard.version%""")
        param("datastandard.version", "3.2c")
    }

    subProject(MetaEdIntegration_PostgreSQL.Project)
})

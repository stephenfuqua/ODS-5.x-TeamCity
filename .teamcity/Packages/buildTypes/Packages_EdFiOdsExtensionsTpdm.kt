# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package Packages.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*

object Packages_EdFiOdsExtensionsTpdm : BuildType({
    templates(Packages_EdFiExtensionsTemplate)
    name = "EdFi.Ods.Extensions.TPDM"

    params {
        param("script.initdev.parameters", "-NoCredentials -NoDeploy")
        param("extension.project", "EdFi.Ods.Extensions.TPDM")
        param("nuget.pack.files", """Ed-Fi-Extensions\Extensions\EdFi.Ods.Extensions.TPDM\EdFi.Ods.Extensions.TPDM.nuspec""")
        param("nuget.package.name", "EdFi.Ods.Extensions.TPDM")
        param("script.create.template", """Ed-Fi-ODS-Implementation\DatabaseTemplate\Modules\create-minimal-template.psm1""")
        param("PackageId", "EdFi%odsapi.package.suffix%.Ods.Extensions.TPDM")
        param("nuget.package.description", "EdFi.Ods.Extensions.TPDM")
    }
})

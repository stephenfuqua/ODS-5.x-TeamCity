# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

package Packages.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.FileContentReplacer
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.replaceContent

object Packages_EdFiOdsExtensionsHomograph : BuildType({
    templates(Packages_EdFiExtensionsTemplate)
    name = "EdFi.Ods.Extensions.Homograph"

    params {
        param("script.initdev.parameters", "-NoDeploy")
        param("extension.project", "EdFi.Ods.Extensions.Homograph")
        param("nuget.pack.files", """Ed-Fi-Extensions\Extensions\EdFi.Ods.Extensions.Homograph\EdFi.Ods.Extensions.Homograph.nuspec""")
        param("git.branch.default", "main")
        param("nuget.package.name", "EdFi.Ods.Extensions.Homograph")
        param("script.create.template", """Ed-Fi-ODS-Implementation\DatabaseTemplate\Modules\create-minimal-template.psm1""")
        param("PackageId", "EdFi%odsapi.package.suffix%.Ods.Extensions.Homograph")
        param("nuget.package.description", "EdFi.Ods.Extensions.Homograph")
    }

    features {
        replaceContent {
            id = "BUILD_EXT_58"
            fileRules = "Ed-Fi-Extensions/Extensions/EdFi.Ods.Extensions.Homograph/*.nuspec"
            pattern = """(?<=<id>)(.*?)(EdFi)(?=\b.*</id>)"""
            replacement = "%odsapi.package.suitenumber%"
            encoding = FileContentReplacer.FileEncoding.UTF_8
            customEncodingName = "UTF-8"
        }
    }
})

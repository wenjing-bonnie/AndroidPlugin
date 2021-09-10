package com.wj.manifest

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 插件入口
 */
class ManifestProject implements Plugin<Project> {

    @Override
    void apply(Project project) {
        SystemOutPrint.println("Welcome ManifestProject")
        project.extensions.findByType(BaseExtension.class).variantFilter {
            SystemOutPrint.println(it.name)
        }
    }

}
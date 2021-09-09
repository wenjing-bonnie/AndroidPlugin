package com.wj.manifest

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 插件入口
 */
class ManifestProject implements Plugin<Project> {

    @Override
    void apply(Project project) {
        SystemOutPrint.println("Welcome ManifestProject")
    }
}
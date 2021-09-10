package com.wj.manifest

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * 插件入口
 */
class ManifestProject implements Plugin<Project> {
    List variantManifestTaskNames = new ArrayList()

    @Override
    void apply(Project project) {
        SystemOutPrint.println("Welcome ManifestProject")
        addExportTaskForManifest(project)
        project.extensions.findByType(AppExtension.class).variantFilter {
            SystemOutPrint.println("variant = " + it.name)
            variantManifestTaskNames.add(String.format("process%sManifest", it.name))
        }
    }

    void addExportTaskForManifest(Project project) {
        Task task = project.getTasks().create(AddExportForManifestTask.TAG, AddExportForManifestTask)
        project.afterEvaluate {
            project.getTasks().matching {
                for (int i = 0; i < variantManifestTaskNames.size(); i++) {
                    it.name.equals(variantManifestTaskNames.get(i))
                }
            }.each {
                SystemOutPrint.println(" == afterEvaluate == " + it.name)
                it.dependsOn(task)
            }
        }
    }

}
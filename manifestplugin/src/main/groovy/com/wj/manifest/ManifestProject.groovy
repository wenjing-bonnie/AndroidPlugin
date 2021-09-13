package com.wj.manifest

import com.android.build.gradle.AppExtension
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
        SystemPrint.outPrintln("Welcome ManifestProject")
        getAllVariantManifestTask(project)
        addExportTaskForManifest(project)
    }
    /**
     * 获取所有的变体相关的process%sManifest任务名称
     * @param project
     */
    void getAllVariantManifestTask(Project project) {
        project.extensions.findByType(AppExtension.class).variantFilter {
            variantManifestTaskNames.add(String.format("process%sManifest", it.name.capitalize()))
        }
    }
    /**
     * 将添加android:exported的task添加到任务队列中
     * @param project
     */
    void addExportTaskForManifest(Project project) {
        Task task = project.getTasks().create(AddExportForManifestTask.TAG, AddExportForManifestTask)
        project.afterEvaluate {
            project.getTasks().each {
                if (it.name.startsWith("process") && it.name.endsWith("Manifest")) {
                    for (int i = 0; i < variantManifestTaskNames.size(); i++) {
                        if (it.name.equals(variantManifestTaskNames.get(i))) {
                            //在process%sManifest添加解析xml文件的task
                            it.dependsOn(task)
                        }
                    }
                }
            }

        }
    }

}
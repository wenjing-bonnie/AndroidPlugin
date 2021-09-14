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
     * // processDebugManifest：生成最终 AndroidManifest 文件
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
                //在process%sManifest任务之后
                if (it.name.startsWith("process") && it.name.endsWith("Manifest")) {
                    for (int i = 0; i < variantManifestTaskNames.size(); i++) {
                        if (it.name.equals(variantManifestTaskNames.get(i))) {
                            //在process%sManifest添加解析xml文件的task
                            //Circular dependency between the following tasks:
                            //it.dependsOn(task)
                            //mustRunAfter并不会添加依赖，只是高度Gradle执行的优先级
                            //无论是shouldRunAfter还是mustRunAfter，影响的只是task在队列中的顺序，并不影响任何任务间的执行依赖，也就是说使用shouldRunAfter和mustRunAfter并不会导致任务队列中添加新的task。
                            //task.mustRunAfter(it)
                            SystemPrint.errorPrintln("进入了 process Manifest")
                            //TODO 这种方式只能添加在这个process%sManifest之前
                             //it.dependsOn(task)
                            //现在还行task1 在执行task2
                            //task2.dependsOn(task1)
                            //用于任务执行结束后自动执行其他任务。taskC.finalizedBy taskD 执行完taskC后执行taskD
                            it.finalizedBy(task)
//                            it.doLast {
//                                SystemPrint.errorPrintln(it.name + " 执行完毕 ")
//                            }
                        }
                    }
                }
                //引用第三方sdk里面有一些多申请的权限，我们也可以在这边直接剔除掉，这里选择的切入点在processResources的任务执行之前，
                //在processResources任务之前，这个发现只有在非app module中才有这个processResources task
//                if (it.name.startsWith("processResources")) {
//                    SystemPrint.errorPrintln("进入了 processResources")
//                    it.dependsOn(task)
//                }
            }
        }
    }

}
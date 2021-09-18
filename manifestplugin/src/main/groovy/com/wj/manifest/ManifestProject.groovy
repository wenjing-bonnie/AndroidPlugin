package com.wj.manifest

import com.android.build.gradle.AppExtension
import com.android.build.gradle.tasks.ProcessApplicationManifest
import com.android.build.gradle.tasks.ProcessMultiApkApplicationManifest
import com.wj.manifest.task.AddExportForEveryPackageManifestTask
import com.wj.manifest.task.SetLastVersionInfoTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 插件入口
 */
class ManifestProject implements Plugin<Project> {
    List variantNames = new ArrayList()

    @Override
    void apply(Project project) {
        SystemPrint.outPrintln("Welcome ManifestProject")
        createManifestExtension(project)
        getAllVariantManifestTask(project)
        addTaskForEveryVariantAfterEvaluate(project)
    }
    /**
     * 获取所有的变体相关的process%sManifest任务名称
     * @param project
     */
    void getAllVariantManifestTask(Project project) {
        project.extensions.findByType(AppExtension.class).variantFilter {
            variantNames.add(it.name)
        }
        project.afterEvaluate {
            ManifestExtension extension = project.getExtensions().findByName(ManifestExtension.TAG)
            if (extension.versionFile != null) {
                SystemPrint.outPrintln("versionPath" + extension.versionFile.getAbsolutePath())
            }
        }
    }

    /**
     * 配置扩展属性
     * @param project
     */
    void createManifestExtension(Project project) {
        project.getExtensions().create(ManifestExtension.TAG, ManifestExtension)
    }

    /**
     * 在项目配置完成之后添加task
     * @param project
     */
    void addTaskForEveryVariantAfterEvaluate(Project project) {
        AddExportForEveryPackageManifestTask beforeAddTask = project.getTasks().create(AddExportForEveryPackageManifestTask.TAG,
                AddExportForEveryPackageManifestTask)
        SetLastVersionInfoTask versionTask = project.getTasks().create(SetLastVersionInfoTask.TAG, SetLastVersionInfoTask)
        //在项目配置完成后,添加自定义Task
        project.afterEvaluate {
            variantNames.each {
                //直接通过task的名字找到ProcessApplicationManifest这个task
                addExportTaskForEveryPackageManifest(project, beforeAddTask, it)
                addVersionTaskForMergedManifest(project, versionTask, it)
            }
        }
    }

    /**
     * 为所有依赖的包的AndroidManifest添加android:exported
     * processHuaweiDebugMainManifest:合并所有依赖包以及主module中的AndroidManifest文件
     * processDebugManifest:为所有变体生成最终AndroidManifest文件
     * 不能使用ProcessDebugManifest.因为processHuaweiDebugMainManifest执行的时候就报错,还未执行到ProcessDebugManifest
     * @param project
     */
    void addExportTaskForEveryPackageManifest(Project project, AddExportForEveryPackageManifestTask beforeAddTask, String it) {
        //找到processHuaweiDebugMainManifest，在这个之前添加export
        ProcessApplicationManifest processManifestTask = project.getTasks().getByName(String.format("process%sMainManifest", it.capitalize()))
        beforeAddTask.setManifestsFileCollection(processManifestTask.getManifests())
        beforeAddTask.setMainManifestFile(processManifestTask.getMainManifest().get())
        processManifestTask.dependsOn(beforeAddTask)
    }

    /**
     * 添加处理版本信息的Task
     * @param project
     */
    void addVersionTaskForMergedManifest(Project project, SetLastVersionInfoTask versionTask, String it) {
        //在项目配置完成后,添加自定义Task
        //方案一:直接通过task的名字找到ProcessMultiApkApplicationManifest这个task
        //直接找到ProcessDebugManifest,然后在执行后之后执行该Task
        ProcessMultiApkApplicationManifest processManifestTask = project.getTasks().getByName(String.format("process%sManifest", it.capitalize()))
        processManifestTask.finalizedBy(versionTask)
    }

}
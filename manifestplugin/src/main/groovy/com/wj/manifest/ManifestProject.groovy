package com.wj.manifest

import com.android.build.gradle.tasks.ProcessApplicationManifest
import com.android.build.gradle.tasks.ProcessMultiApkApplicationManifest
import com.wj.manifest.task.AddExportForPackageManifestTask
import com.wj.manifest.task.SetLastVersionInfoTask
import com.wj.manifest.utils.SystemPrint
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 插件入口
 */
class ManifestProject implements Plugin<Project> {
    final String DEFAULT_VARIANT = "Debug"
    String variantName

    @Override
    void apply(Project project) {
        SystemPrint.outPrintln("Welcome ManifestProject")
        createManifestExtension(project)
        getCurrentBuildVariantName(project)
        addTaskForVariantAfterEvaluate(project)
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
    void addTaskForVariantAfterEvaluate(Project project) {
        //初始化 AddExportForPackageManifestTask
        AddExportForPackageManifestTask addExportTask = project.getTasks().create(AddExportForPackageManifestTask.TAG,
                AddExportForPackageManifestTask)
        //初始化 SetLastVersionInfoTask
        SetLastVersionInfoTask versionTask = project.getTasks().create(SetLastVersionInfoTask.TAG, SetLastVersionInfoTask)
        versionTask.setVariantName(variantName)
        //在项目配置完成后,添加自定义Task
        project.afterEvaluate {
            //为当前变体的task都加入到这个任务队列中。
            //所以通过project.getTasks().each {}去匹配每个task的startsWith&&endsWith的逻辑是一致的
            //并且这种性能会更高
            //直接通过task的名字找到ProcessApplicationManifest这个task
            addExportTaskForPackageManifest(project, addExportTask)
            addVersionTaskForMergedManifest(project, versionTask)
        }
    }

    /**
     * 为所有依赖的包的AndroidManifest添加android:exported
     * processHuaweiDebugMainManifest:合并所有依赖包以及主module中的AndroidManifest文件
     * processDebugManifest:为所有变体生成最终AndroidManifest文件
     * 不能使用ProcessDebugManifest.因为processHuaweiDebugMainManifest执行的时候就报错,还未执行到ProcessDebugManifest
     * @param project
     */
    void addExportTaskForPackageManifest(Project project, AddExportForPackageManifestTask beforeAddTask) {
        //找到processHuaweiDebugMainManifest，在这个之前添加export
        ProcessApplicationManifest processManifestTask = project.getTasks().getByName(String.format("process%sMainManifest", variantName))
        beforeAddTask.setManifestsFileCollection(processManifestTask.getManifests())
        beforeAddTask.setMainManifestFile(processManifestTask.getMainManifest().get())
        processManifestTask.dependsOn(beforeAddTask)
    }

    /**
     * 添加处理版本信息的Task
     * @param project
     */
    void addVersionTaskForMergedManifest(Project project, SetLastVersionInfoTask versionTask) {
        //在项目配置完成后,添加自定义Task
        //方案一:直接通过task的名字找到ProcessMultiApkApplicationManifest这个task
        //直接找到ProcessDebugManifest,然后在执行后之后执行该Task
        ProcessMultiApkApplicationManifest processManifestTask = project.getTasks().getByName(String.format("process%sManifest", variantName))
        versionTask.setManifestFilePath(processManifestTask.getMainMergedManifest().asFile.get().getAbsolutePath())
        processManifestTask.finalizedBy(versionTask)
    }


    /**
     *
     * 找到当前的variant，然后在该变体基础上创建各个task。需要验证如果是多个变体打包过程是否可以 debug release
     *
     * @param project
     * @return "HuaweiDebug"\"Debug"...
     */
    void getCurrentBuildVariantName(Project project) {
        String parameter = project.gradle.getStartParameter().getTaskRequests().toString()
        SystemPrint.outPrintln(parameter)
        //assemble(\w+)(Release|Debug)仅提取Huawei
        String regex = parameter.contains("assemble") ? "assemble(\\w+)" : "generate(\\w+)"
        Pattern pattern = Pattern.compile(regex)
        Matcher matcher = pattern.matcher(parameter)
        if (matcher.find()) {
            //group（0）就是指的整个串，group（1） 指的是第一个括号里的东西，group（2）指的第二个括号里的东西
            SystemPrint.outPrintln(matcher.group(1))
            variantName = matcher.group(1)
        }
        if (variantName == null || variantName.length() == 0) {
            variantName = DEFAULT_VARIANT
        }
    }

}
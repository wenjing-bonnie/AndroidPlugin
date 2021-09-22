package com.wj.manifest

import com.android.build.gradle.AppExtension
import com.android.build.gradle.tasks.ProcessApplicationManifest
import com.android.build.gradle.tasks.ProcessMultiApkApplicationManifest
import com.wj.manifest.task.AddExportForPackageManifestTask
import com.wj.manifest.task.SetLastVersionTask
import com.wj.manifest.utils.SystemPrint
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 插件入口
 */
class ManifestProject implements Plugin<Project> {
    String variantName
    protected List variantInsteadNames = new ArrayList<>()

    @Override
    void apply(Project project) {
        //创建ManifestExtension
        createManifestExtension(project)
        //在sync中无法获取到variantName
        getVariantNameInBuild(project)

        SystemPrint.outPrintln(String.format("Welcome %s ManifestProject", variantName))
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
        SetLastVersionTask versionTask = project.getTasks().create(SetLastVersionTask.TAG, SetLastVersionTask)
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
    void addVersionTaskForMergedManifest(Project project, SetLastVersionTask versionTask) {
        //在项目配置完成后,添加自定义Task
        //方案一:直接通过task的名字找到ProcessMultiApkApplicationManifest这个task
        //直接找到ProcessDebugManifest,然后在执行后之后执行该Task
        ProcessMultiApkApplicationManifest processManifestTask = project.getTasks().getByName(String.format("process%sManifest", variantName))
        versionTask.setManifestFilePath(processManifestTask.getMainMergedManifest().asFile.get().getAbsolutePath())
        processManifestTask.finalizedBy(versionTask)
    }


    /**
     * 获取当前变体名
     * (1)在执行build任务的时候,
     * project.gradle.getStartParameter().getTaskRequests()返回的内容:[DefaultTaskExecutionRequest{args=[:wjplugin:assemble, :wjplugin:testClasses, :manifestplugin:assemble, :manifestplugin:testClasses, :firstplugin:assemble, :firstplugin:testClasses, :app:assembleHuaweiDebug],projectPath='null'}]
     * 可从该字符串中截取当前的variant，然后在该变体基础上创建各个task.
     * (2)在执行sync任务的时候,
     * project.gradle.getStartParameter().getTaskRequests()返回的内容:[DefaultTaskExecutionRequest{args=[],projectPath='null'}]
     * 解决方案:通过project.extensions.findByType(AppExtension.class)找到一个可用的变体(因为会将所有的变体task都加入到任务队列中),将该变体作为变体名来执行完sync任务(仅仅为了完成sync任务,没有任何意义,在执行build任务的时候还会通过{@link #getVariantNameInBuild}替换掉逻辑)
     * 但是最理想的解决方案是该在sync的时候,可以不执行该插件(判断逻辑就是获取的variantName为null的时候,{@link #apply()}直接返回即可)
     *
     * TODO 需要验证在debug release多个变体打包过程
     * @param project
     * @return "HuaweiDebug"\"Debug"...
     */
    void getVariantNameInBuild(Project project) {
        String parameter = project.gradle.getStartParameter().getTaskRequests().toString()
        SystemPrint.outPrintln("" + parameter)
        //assemble(\w+)(Release|Debug)仅提取Huawei
        String regex = parameter.contains("assemble") ? "assemble(\\w+)" : "generate(\\w+)"
        Pattern pattern = Pattern.compile(regex)
        Matcher matcher = pattern.matcher(parameter)
        if (matcher.find()) {
            //group（0）就是指的整个串，group（1） 指的是第一个括号里的东西，group（2）指的第二个括号里的东西
            variantName = matcher.group(1)
        }
        if (!checkValidVariantName()){
            //从AppExtension中获取所有变体,作为获取当前变体的备用方案
            getVariantNameFromAllVariant(project)
        }
    }

    /**
     * 获取所有的变体中的一个可用的变体名,仅仅用来保证sync任务可执行而已
     * project.extensions.findByType()有执行时机,所以会出现在getVariantNameInBuild()中直接调用getVariantNameFromAllVariant()将无法更新variantName
     *
     * @param project
     */
    void getVariantNameFromAllVariant(Project project) {
        if (checkValidVariantName()) {
            return
        }
        //但是sync时返回的内容:[DefaultTaskExecutionRequest{args=[],projectPath='null'}],其实该过程可以不执行该插件也可以
        //直接从所有的变体中取一个可用的变体名,返回
        //
        project.extensions.findByType(AppExtension.class).variantFilter {
            variantName = it.name.capitalize()
            if (checkValidVariantName()) {
                return true
            }
        }
    }

    boolean checkValidVariantName() {
        variantName != null && variantName.length() > 0
    }

}
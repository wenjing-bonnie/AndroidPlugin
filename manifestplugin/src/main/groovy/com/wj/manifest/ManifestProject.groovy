package com.wj.manifest

import com.android.build.gradle.AppExtension
import com.android.build.gradle.tasks.ProcessApplicationManifest
import com.android.build.gradle.tasks.ProcessMultiApkApplicationManifest
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
        getAllVariantManifestTask(project)
        //addExportTaskForMergedManifest(project)
        addExportTaskForEveryPackageManifest(project)
    }
    /**
     * 获取所有的变体相关的process%sManifest任务名称
     * // processDebugManifest：生成最终 AndroidManifest 文件
     * @param project
     */
    void getAllVariantManifestTask(Project project) {
        project.extensions.findByType(AppExtension.class)
                .variantFilter {
                    variantNames.add(it.name)
                }
    }
    /**
     * 为所有依赖的包的AndroidManifest添加android:exported
     * @param project
     */
    void addExportTaskForEveryPackageManifest(Project project) {
        AddExportForEveryPackageManifestTask beforeAddTask = project.getTasks().create(AddExportForEveryPackageManifestTask.TAG,
                AddExportForEveryPackageManifestTask)
        //在项目配置完成后,添加自定义Task
        project.afterEvaluate {
            //直接通过task的名字找到ProcessApplicationManifest这个task
            variantNames.each {
                //找到processHuaweiDebugMainManifest，在这个之前添加export
                ProcessApplicationManifest processManifestTask = project.getTasks().getByName(String.format("process%sMainManifest", it.capitalize()))
//                processManifestTask.doFirst {
//                    processManifestTask.getManifests().each {
//                        printManifest(it)
//                    }
//                    printManifest(processManifestTask.getMainManifest().get())
//                }
                beforeAddTask.setManifestsFileCollection(processManifestTask.getManifests())
                beforeAddTask.setMainManifestFile(processManifestTask.getMainManifest().get())
                processManifestTask.dependsOn(beforeAddTask)
            }
        }
    }

    /**
     * 将添加android:exported的task添加到任务队列中
     * @param project
     */
    void addExportTaskForMergedManifest(Project project) {
        AddExportForMergedManifestTask addTask = project.getTasks().create(AddExportForMergedManifestTask.TAG, AddExportForMergedManifestTask)
        addTask.setVariantNames(variantNames)
        //在项目配置完成后,添加自定义Task
        project.afterEvaluate {
            //方案一:直接通过task的名字找到ProcessApplicationManifest这个task
            variantNames.each {
                //直接找到ProcessApplicationManifest，然后在执行后之后执行该Task
                ProcessMultiApkApplicationManifest processManifestTask = project.getTasks().getByName(String.format("process%sManifest", it.capitalize()))
                addTask.setManifestFilePath(processManifestTask.getMainMergedManifest().get().getAsFile().getAbsolutePath())
                processManifestTask.finalizedBy(addTask)
            }
            //方案二:从project中的所有task进行匹配
            project.getTasks().each {
                //在process%sManifest任务之后
                if (it.name.startsWith("process") && it.name.endsWith("Manifest")) {
                    for (int i = 0; i < variantNames.size(); i++) {
                        if (it.name.equals(variantNames.get(i))) {

                            //在process%sManifest添加解析xml文件的task
                            //Circular dependency between the following tasks:
                            //it.dependsOn(addTask)
                            //mustRunAfter并不会添加依赖，只是高度Gradle执行的优先级
                            //无论是shouldRunAfter还是mustRunAfter，影响的只是task在队列中的顺序，并不影响任何任务间的执行依赖，也就是说使用shouldRunAfter和mustRunAfter并不会导致任务队列中添加新的task。
                            //addTask.mustRunAfter(it)
                            //SystemPrint.errorPrintln("进入了 process Manifest")
                            //TODO 这种方式只能添加在这个process%sManifest之前
                            //it.dependsOn(addTask)
                            //现在还行task1 在执行task2
                            //task2.dependsOn(task1)
                            //用于任务执行结束后自动执行其他任务。taskC.finalizedBy taskD 执行完taskC后执行taskD
                            //it.finalizedBy(addTask)

                        }
                    }
                }
                //引用第三方sdk里面有一些多申请的权限，我们也可以在这边直接剔除掉，这里选择的切入点在processResources的任务执行之前，
                //在processResources任务之前，这个发现只有在非app module中才有这个processResources addTask
//                if (it.name.startsWith("processResources")) {
//                    SystemPrint.errorPrintln("进入了 processResources")
//                    it.dependsOn(addTask)
//                }
            }
        }
    }


    void printManifest(File manifestFile) {
        XmlParser xmlParser = new XmlParser()
        def node = xmlParser.parse(manifestFile)
        if (manifestFile.getAbsolutePath().endsWith("MobGuardMC-2021.0719.1753/AndroidManifest.xml")
                || manifestFile.getAbsolutePath().endsWith("/main/AndroidManifest.xml")) {
            SystemPrint.errorPrintln("printManifest = " + manifestFile.getAbsolutePath())
            SystemPrint.errorPrintln(node.toString())
        }
    }

}
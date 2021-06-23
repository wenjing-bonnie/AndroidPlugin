package com.wj.plugin

import com.wj.plugin.extension.TemplateSettingExtension
import com.wj.plugin.task.HandleTemplateTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 *
 * Project 相当于一个build.gradle文件
 * 当把该插件通过plugins{}放入到项目中的build.gradle中的时候：
 * 1）在Configure project :app 会首先执行该文件里面的内容，直到把plugins{}里面的插件的Plugin<Project>都执行完毕
 * 2）然后在Configure project :firstplugin 每一个插件的build.gradle，当该插件的build.gradle配置结束回调自身的afterEvaluate{},最后回调到整个项目工程的settings.gradle
 *
 * 所以在apply(Project project) 中的project返回的是app
 * @author wenjing.liu
 */

class FirstPluginProject implements Plugin<Project> {

    @Override
    void apply(Project project) {
        SystemOutPrint.println("================")
        SystemOutPrint.println("First Plugin")
        SystemOutPrint.println("================")
        /**（1）添加 TemplateSettingExtension*/
        createExtensions(project)
        /**（2）将功能的Task添加到app这个project的任务队列中*/
        addHandleTemplateTask(project)
    }
    /**
     * 创建Extension
     * @param project
     */
    void createExtensions(Project project) {
        //在这里是无法取到  extension的值，因为此时还没有构建到app中的build.gradle
        project.getExtensions().create(TemplateSettingExtension.TAG, TemplateSettingExtension)
    }
    /**
     * 将HandleTemplateTask加入到任务队列中
     * @param project
     */
    void addHandleTemplateTask(Project project) {
        Task task = project.getTasks().create("handleTemplateTask", HandleTemplateTask)

        setHandleTemplateTaskInputFromExtension(project)
        //这里是返回的app的这个module，然后在app的project的所有tasks中添加该handleTemplateTask
        project.afterEvaluate {
            project.getTasks().matching {
                //如果将该插件放到'com.android.application',则在"preBuild"之前添加该Task
                it.name.equals("preBuild")
            }.each {
                it.dependsOn(task)

            }
        }
    }

    void setHandleTemplateTaskInputFromExtension(Project project, Task task) {
        TemplateSettingExtension extension = project.getExtensions().findByName(TemplateSettingExtension.TAG)
        task.setFileFormat(".java")
        SystemOutPrint.println("compile SDK = " + extension.compileSdk)
        String interfacePath = extension.interfaceSourceDir
        SystemOutPrint.println("interfaceSourceDir 122  = " + interfacePath)
        String rootPath = project.getProjectDir().getAbsolutePath()
        String path = rootPath + interfacePath
        task.setFileSourceDir(new File(interfacePath))
    }


}
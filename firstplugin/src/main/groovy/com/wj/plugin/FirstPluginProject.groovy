package com.wj.plugin

import com.android.build.gradle.AppExtension
import com.wj.plugin.extension.AndroidExtension
import com.wj.plugin.extension.TemplateSettingExtension
import com.wj.plugin.extension.TemplateSettingExtensionInProject
import com.wj.plugin.task.HandleTemplateTask
import com.wj.plugin.transform.HotTransform
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
 * 所以在apply(Project project) 中的project返回的是app(通过apply或plugins{}添加该插件的工程),
 * 所以通过下面的方式添加的属性扩展和自定义Task都是添加到app这个module上面
 * @author wenjing.liu
 */

class FirstPluginProject implements Plugin<Project> {

    @Override
    void apply(Project project) {
        SystemOutPrint.println("Apply the First Plugin Project")
        /**（1）添加 TemplateSettingExtension
         * 因为添加到被依赖module的project中，所以该属性扩展只能在被依赖module中引用
         * */
        createExtensions(project)
        /**（2）将功能的Task添加到app这个project的任务队列中*/
        addHandleTemplateTask(project)
        /**为添加到build.gradle来增加扩展属性*/
        createExtensionsForInProject(project)
        //测试两层闭包
        createAndroidExtensions(project)
        //隐藏测试输出
        // testAndroidExtension(project)

        /**添加Transform*/
        project.extensions.findByType(AppExtension.class).registerTransform(new HotTransform())
    }

    void createExtensionsForInProject(project) {
        project.getExtensions().create(TemplateSettingExtensionInProject.TAG, TemplateSettingExtensionInProject)
    }


    void createAndroidExtensions(Project project) {
        project.getExtensions().create(AndroidExtension.TAG, AndroidExtension, project)
    }

    /**
     * 创建Extension
     * @param project
     */
    TemplateSettingExtension createExtensions(Project project) {
        //在这里是无法取到  extension的值，因为此时还没有构建到app中的build.gradle
        project.getExtensions().create(TemplateSettingExtension.TAG, TemplateSettingExtension)
    }
    /**
     * 将HandleTemplateTask加入到任务队列中
     * @param project
     */
    void addHandleTemplateTask(Project project) {
        Task task = project.getTasks().create("handleTemplateTask", HandleTemplateTask)

        //这里是返回的app的这个module，然后在app的project的所有tasks中添加该handleTemplateTask
        project.afterEvaluate {
            project.getTasks().matching {
                //如果将该插件放到'com.android.application',则在"preBuild"之前添加该Task
                it.name.equals("preBuild")
            }.each {
                it.dependsOn(task)
                setHandleTemplateTaskInputFromExtension(project, task)
            }
        }
    }
    /**
     * 设置HandleTemplateTask的input
     * @param project
     * @param task
     */
    void setHandleTemplateTaskInputFromExtension(Project project, HandleTemplateTask task) {
        //项目配置完成之后，就可以获得设置的Extension中的内容
        TemplateSettingExtension extension = project.getExtensions().findByName(TemplateSettingExtension.TAG)
        task.setFileFormat(".java")
        String path = project.getProjectDir().getAbsolutePath() + "/src/main/java/mvp"
        task.setFileSourceDir(extension.interfaceSourceDir)
    }

    void testAndroidExtension(Project project) {
        project.afterEvaluate {
            AndroidExtension extension = project.getExtensions().findByName(AndroidExtension.TAG)
            SystemOutPrint.println("compileSdkVersion = " + extension.compileSdkVersion)
            SystemOutPrint.println("applicationId = " + extension.defaultConfig.getApplicationId())
            SystemOutPrint.println("minSdkVersion = " + extension.defaultConfig.minSdkVersion)
            extension.buildTypes.each {
                SystemOutPrint.println("buildTypes = " + it.name + " , signingConfig = " + it.signingConfig)

            }
        }
    }

}
package com.wj.global.gradle

import com.wj.global.log.SystemOutPrint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Project相当于build.gradle
 * @author wenjing.liu
 */
class GlobalGradleProject implements Plugin<Project> {

    @Override
    void apply(Project project) {
        SystemOutPrint.print(" ~~~~~~~~~~~~~~ ")
        SystemOutPrint.print(" Global Gradle Project ")
        SystemOutPrint.print(" ~~~~~~~~~~~~~~ ")

        //addHandleTemplateTask(project)
    }

    void addHandleTemplateTask(Project project) {
        Task task = project.getTasks().create("globalGradleConfig", GlobalGradleConfig)

        //这里是返回的app的这个module，然后在app的project的所有tasks中添加该handleTemplateTask
        project.afterEvaluate {
            project.getTasks().matching {
                //如果将该插件放到'com.android.application',则在"preBuild"之前添加该Task
                it.name.equals("compileJava")
            }.each {
                it.dependsOn(task)
                SystemOutPrint.print(task.name)

            }
        }
    }

}
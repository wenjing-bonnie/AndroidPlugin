package com.wj.global.gradle

import com.wj.global.log.SystemOutPrint
import com.wj.global.task.TemplateTask
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

        //Task task = project.getTasks().create("GlobalGradleConfig", GlobalGradleConfig.class)
//        Task templateTask = project.getTasks().create("TemplateTask", TemplateTask.class)
//        Task templateTask1 = project.getTasks().create("TemplateTask1", com.wj.global.gradle.TemplateTask.class)
//        addTemplateTaskDependsOn(templateTask1,project)
//        templateTask1.doFirst {
//            SystemOutPrint.print(" ~~~~~~templateTask1 ~~~~~~ do First ")
//        }
    }

}
package com.wj.global.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Project相当于build.gradle
 * @author wenjing.liu
 */
public class GlobalGradleProject implements Plugin<Project> {

    @Override
    void apply(Project project) {
        SystemOutPrint.print(" ~~~~~~~~~~~~~~ ")
        SystemOutPrint.print(" Global Gradle Project ")
        SystemOutPrint.print(" ~~~~~~~~~~~~~~ ")

        Task task = project.getTasks().create("GlobalGradleConfig", GlobalGradleConfig.class)
        // project.getTasks().findByName("classes").dependsOn(task)
        task.doLast {
            SystemOutPrint.print(" GlobalGradleConfig do last")
        }
    }
}
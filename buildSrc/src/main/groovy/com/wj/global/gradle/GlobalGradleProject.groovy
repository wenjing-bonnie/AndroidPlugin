package com.wj.global.gradle

import com.wj.global.log.SystemOutPrint
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Project相当于build.gradle
 * @author wenjing.liu
 */
class GlobalGradleProject implements Plugin<Project> {

    @Override
    void apply(Project project) {
        SystemOutPrint.print("Apply the Global Gradle Project " + project.name)
        addTemplateTask(project)
    }

    void addTemplateTask(Project project) {
        TemplateTask templateTask = project.getTasks().create("templateTask", TemplateTask)

        project.afterEvaluate {
            it.name.equals("preBuild")
        }.each {
            it.dependsOn(templateTask)
        }
    }


}
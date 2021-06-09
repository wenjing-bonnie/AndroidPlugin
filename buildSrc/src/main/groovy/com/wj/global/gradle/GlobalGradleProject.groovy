package com.wj.global.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project


public class GlobalGradleProject implements Plugin<Project> {

    @Override
    void apply(Project project) {
        SystemOutPrint.print(" ~~~~~~~~~~~~~~ ")
        SystemOutPrint.print(" Global Gradle Project ")
        SystemOutPrint.print(" ~~~~~~~~~~~~~~ ")

        project.getTasks().create("GlobalGradleConfig", GlobalGradleConfig.class);
    }
}
package com.wj.plugin

import com.wj.plugin.config.FirstPluginConfigTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class FirstPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        System.out.println("================")
        System.out.println("First Plugin")
        System.out.println("================")
        FirstPluginConfigTask task = project.getTasks().create("firstPluginConfig", FirstPluginConfigTask.class);
        task.doFirst {
            System.out.println("name = " + task.sdkVersion)
        }
        task.doLast {
            System.out.println("name = " + task.sdkVersion)
        }
    }
}
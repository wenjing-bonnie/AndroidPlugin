package com.wj.plugin

import com.wj.plugin.extension.TemplateSettingExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer

/**
 *
 * Project 相当于一个build.gradle文件
 *
 * @author wenjing.liu
 */

class FirstPluginProject implements Plugin<Project> {
    @Override
    void apply(Project project) {
        SystemOutPrint.println("================")
        SystemOutPrint.println("First Plugin")
        SystemOutPrint.println("================")
        project.getExtensions().create("templateSettingExtension", TemplateSettingExtension)
    }


}
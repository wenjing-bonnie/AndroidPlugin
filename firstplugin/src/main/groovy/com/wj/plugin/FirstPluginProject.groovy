package com.wj.plugin

import com.wj.plugin.task.FirstPluginConfigTask
import com.wj.plugin.task.HandleTemplateTask
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

        //2.通过继承DefaultTask，创建一个Task
        //获取当前project中的TaskContainer
        TaskContainer taskContainer = project.getTasks()
        //create()：Creates a Task with the given name and adds it to this container.
        //FirstPluginConfigTask task = taskContainer.create("firstPluginConfig", FirstPluginConfigTask.class)
       // HandleTemplateTask handleTemplateTask = taskContainer.create("handleTemplateTask", HandleTemplateTask.class)
    }
}
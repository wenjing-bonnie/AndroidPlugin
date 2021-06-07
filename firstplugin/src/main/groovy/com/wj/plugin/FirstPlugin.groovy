package com.wj.plugin

import com.wj.plugin.config.FirstPluginConfigTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.tasks.TaskContainer

class FirstPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        System.out.println("================")
        System.out.println("First Plugin")
        System.out.println("================")

        //这种方式可以执行输入出
        project.task("hellotask") {
            println(" project hello task =======")

        }
        //获取当前project中的TaskContainer
        TaskContainer taskContainer = project.getTasks()
        //create()：Creates a Task with the given name and adds it to this container.
        FirstPluginConfigTask task = taskContainer.create("firstPluginConfig", FirstPluginConfigTask.class)
        task.doFirst {
            System.out.println("FirstPluginConfigTask doFirst name = " + task.sdkVersion)
        }
        task.doLast {
            System.out.println("FirstPluginConfigTask doLast name = " + task.sdkVersion)
        }
        System.out.println(" ==== " + taskContainer.getByName("firstPluginConfig").name)

        ConfigurationContainer configurationContainer = project.getConfigurations()
        //String ss = configurationContainer.getByName("firstConfiguration").description
        //System.out.println("first configuration name  = " + ss)
        DependencyHandler dependencyHandler = project.getDependencies()
        //dependencyHandler.a
        RepositoryHandler repositoryHandler = project.getRepositories()
        ArtifactHandler artifactHandler = project.getArtifacts()
        //configurationContainer.
        System.out.println("" + dependencyHandler.gradleApi().version)
        //System.out.println(""+dependencyHandler.)
        // System.out.println(taskContainer)
        System.out.println("================")
    }
}
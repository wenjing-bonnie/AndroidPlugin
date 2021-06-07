package com.wj.plugin

import com.wj.plugin.task.FirstPluginConfigTask
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
        //获取当前project中的TaskContainer
        TaskContainer taskContainer = project.getTasks()
        //create()：Creates a Task with the given name and adds it to this container.
        FirstPluginConfigTask task = taskContainer.create("firstPluginConfig", FirstPluginConfigTask.class)
        ConfigurationContainer configurationContainer = project.getConfigurations()
        DependencyHandler dependencyHandler = project.getDependencies()
        RepositoryHandler repositoryHandler = project.getRepositories()
        ArtifactHandler artifactHandler = project.getArtifacts()

    }
}
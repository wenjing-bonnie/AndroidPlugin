package com.android.wj.debug

import com.android.build.gradle.AppExtension
import com.android.wj.debug.transform.AutoLogTransform
import com.android.wj.debug.utils.SystemOutPrintln
import org.gradle.api.Plugin
import org.gradle.api.Project

class WjDebugPluginProject implements Plugin<Project> {
    @Override
    void apply(Project project) {
        SystemOutPrintln.println("Welcome to WjDebugPluginProject")
        project.extensions.findByType(AppExtension.class).registerTransform(new AutoLogTransform())
    }
}
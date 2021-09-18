package com.wj.manifest

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by wenjing.liu on 2021/9/18 in J1.
 *
 * @author wenjing.liu
 */
class ManifestProjectByKotlin : Plugin<Project> {

    override fun apply(p0: Project) {
        SystemPrintByKotlin.outPrintln("Welcome ManifestProjectByKotlin")
    }
}
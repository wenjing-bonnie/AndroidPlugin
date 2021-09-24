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

        createExtension(p0)
    }

    private fun createExtension(project: Project) {
        project.extensions.create(
            ManifestExtensionByKotlin.TAG,
            ManifestExtensionByKotlin::class.javaObjectType
        )
        //Boolean::class.java指向的时候kotlin标准库里面定义的类Boolean.kt类
        //Boolean::class.javaObjectType指向的是 kotlin 标准库中 JvmClassMappingKt 中的一个方法，即Boolean.java类

    }
}
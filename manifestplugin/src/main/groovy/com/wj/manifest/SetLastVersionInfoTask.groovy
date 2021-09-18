package com.wj.manifest

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by wenjing.liu on 2021/9/17 in J1.
 * 读取配置文件(含有对每个版本的信息描述)中的versionCode与versionName
 * 然后在加上360的自动打包流程
 * 用来复写下Extension的定义 以及@InputFile
 * @author wenjing.liu
 */
class SetLastVersionInfoTask extends DefaultTask {
    protected static final String TAG = "SetLastVersionInfoTask"

    @TaskAction
    void run() {
        SystemPrint.outPrintln("Running ...")
    }
}
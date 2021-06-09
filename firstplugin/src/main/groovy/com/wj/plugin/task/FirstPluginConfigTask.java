package com.wj.plugin.task;

import com.wj.plugin.SystemOutPrint;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

/**
 * Created by wenjing.liu on 2021/6/3 in J1.
 * 可在build.gradle引用该Task的时候的配置选项
 *
 * @author wenjing.liu
 */
public class FirstPluginConfigTask extends DefaultTask {


    public String sdkVersion;

    @Input
    public void setSdkVersion(String sdk) {
        this.sdkVersion = sdk;
        SystemOutPrint.println("FirstPluginConfigTask @Input sdk = " + sdk);
    }

    @TaskAction
    public void taskAction() {
        SystemOutPrint.println("FirstPluginConfigTask  @TaskAction  setSdkVersion ");
    }

}

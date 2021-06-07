package com.wj.plugin.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

/**
 * Created by wenjing.liu on 2021/6/3 in J1.
 *
 * @author wenjing.liu
 */
public class FirstPluginConfigTask extends DefaultTask {


    public String sdkVersion;

    @Input
    public void setSdkVersion(String sdk) {
        this.sdkVersion = sdk;
        System.out.println("FirstPluginConfigTask task  === "+sdk);
    }

    @TaskAction
    public void setSdkVersion() {
        System.out.println("FirstPluginConfigTask task  setSdkVersion === ");
    }

}

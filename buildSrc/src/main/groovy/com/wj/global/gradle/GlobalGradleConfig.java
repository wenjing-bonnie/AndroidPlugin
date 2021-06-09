package com.wj.global.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;


/**
 * Created by wenjing.liu on 2021/6/9 in J1.
 *
 * @author wenjing.liu
 */
public class GlobalGradleConfig extends DefaultTask {
    String version;

    @Input
    public void setVersion(String version) {
        this.version = version;
       // PrintKotlin.print(" version = " + version);
        SystemOutPrint.print(" version = " + version);
    }

    @TaskAction
    public void taskAction() {
        //PrintKotlin.print(" taskAction ");
        SystemOutPrint.print(" taskAction ");
    }

}

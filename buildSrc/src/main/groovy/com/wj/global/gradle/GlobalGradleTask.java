package com.wj.global.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * Created by wenjing.liu on 2021/6/9 in J1.
 *
 * 一个Task
 * @author wenjing.liu
 */
public class GlobalGradleTask extends DefaultTask {

    @TaskAction
    public void taskAction() {
        SystemOutPrint.print("task action in Default Task");
    }
}

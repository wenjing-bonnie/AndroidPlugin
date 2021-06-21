package com.wj.plugin.task;

import com.wj.plugin.SystemOutPrint;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

/**
 * Created by wenjing.liu on 2021/6/9 in J1.
 * <p>
 * 一个Task:实现一个定义的接口类来实现对应的实现类的功能
 * 分析：
 * input：一个接口文件
 * output：一个实现类
 * action：实现具体的实现功能
 *
 * @author wenjing.liu
 */
public class HandleTemplateTask extends DefaultTask {
    private String compileSdk;
    private File interfaceSourceDir;


//    public String getCompileSdk() {
//        SystemOutPrint.println(" compileSdk = " + compileSdk);
//        return compileSdk;
//    }

    /**
     * 必须有setter方法,否则在build.gradle配置该项的时候会识别不到
     */
    @Input
    public void setCompileSdk(String sdk) {
        this.compileSdk = sdk;
    }


//    public void setInterfaceSourceDir(File interfaceSourceDir) {
//        this.interfaceSourceDir = interfaceSourceDir;
//    }
//
//    public File getInterfaceSourceDir() {
//        return interfaceSourceDir;
//    }

    @TaskAction
    public void taskAction() {
        SystemOutPrint.println("task action in Default Task");
    }
}

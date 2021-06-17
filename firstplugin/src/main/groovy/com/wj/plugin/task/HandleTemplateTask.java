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
 * 一个Task
 * <p>
 * TODO 暂时发现只能放到该目录下，其他的module才可以引用该Task
 *
 * @author wenjing.liu
 */
public class HandleTemplateTask extends DefaultTask {
    private String compileSdk;
    private File inputFile;
    //private TemplateEngine


    public String getCompileSdk() {
        SystemOutPrint.println(" compileSdk = " + compileSdk);
        return compileSdk;
    }

    /**
     * 必须有setter方法
     */
    @Input
    public void setCompileSdk(String sdk) {
        this.compileSdk = sdk;
    }

    // @InputFile
    public File getInputFile() {
        SystemOutPrint.println(" inputFile = " + inputFile.getPath());
        return inputFile;
    }


    //@InputFile
    public void taskInput(File file) {
        this.getInputs().file(file);
    }

    @TaskAction
    public void taskAction() {
        SystemOutPrint.println("task action in Default Task");
    }
}

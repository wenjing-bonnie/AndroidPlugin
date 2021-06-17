package com.wj.global.task;

import com.wj.global.log.SystemOutPrint;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

/**
 * Created by wenjing.liu on 2021/6/9 in J1.
 * <p>
 * 一个Task
 *
 * @author wenjing.liu
 */
public class TemplateTask extends DefaultTask {
    private String compileSdk;
    private File inputFile;
    //private TemplateEngine

    @Input
    public String getCompileSdk() {
        SystemOutPrint.print(" compileSdk = " + compileSdk);
        return compileSdk;
    }

    @InputFile
    public File getInputFile() {
        SystemOutPrint.print(" inputFile = " + inputFile.getPath());
        return inputFile;
    }


    @InputFile
    public void taskInput(File file) {
        this.getInputs().file(file);
    }

    @TaskAction
    public void taskAction() {
        SystemOutPrint.print("task action in Default Task");
    }
}

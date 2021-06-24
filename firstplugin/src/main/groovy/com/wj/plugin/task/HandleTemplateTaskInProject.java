package com.wj.plugin.task;

import com.wj.plugin.SystemOutPrint;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

/**
 * Created by wenjing.liu on 2021/6/3 in J1.
 * 可在本插件module的build.gradle添加该Task
 *
 * @author wenjing.liu
 */
public class HandleTemplateTaskInProject extends DefaultTask {
    /**
     * 文件格式
     */
    private String fileFormat;
    /**
     * 文件的路径
     */
    private File fileSourceDir;


    @Input
    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    @InputDirectory
    @Optional
    public File getFileSourceDir() {
        return fileSourceDir;
    }

    public void setFileSourceDir(File fileSourceDir) {
        this.fileSourceDir = fileSourceDir;
    }

    @TaskAction
    public void run() {
        SystemOutPrint.println(" HandleTemplateTaskInProject is running ");
        SystemOutPrint.println(" Set the file format is \" " + getFileFormat());
        SystemOutPrint.println(" Set the file source dir is \" " + getFileSourceDir());
    }
}

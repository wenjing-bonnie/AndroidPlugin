package com.wj.plugin.task;

import com.wj.plugin.SystemOutPrint;

import org.apache.tools.ant.Task;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Optional;
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
    //@Optional 可添加表示参数可选
    public File getFileSourceDir() {
        return fileSourceDir;
    }

    public void setFileSourceDir(File fileSourceDir) {
        this.fileSourceDir = fileSourceDir;
    }

    @TaskAction
    public void run() {
        SystemOutPrint.println(" HandleTemplateTask is running ");
    }
}

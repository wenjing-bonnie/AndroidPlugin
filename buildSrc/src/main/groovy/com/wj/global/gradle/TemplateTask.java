package com.wj.global.gradle;

import com.wj.global.log.SystemOutPrint;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

/**
 * Created by wenjing.liu on 2021/6/9 in J1.
 * <p>
 * 一个Task
 * TODO 1.暂时发现自定义的Task只能放到该目录下，其他的module中才可以引用该Task:
 * TODO   即 com.wj.global.task.TemplateOutTask就不能被app这个module下build.gradle通过"task taskActionTask(type:  TemplateOutTask)"来使用该Task
 * TODO 2.该TemplateTask并且不能放到当前build.gradle文件下通过"task taskActionTask(type:  TemplateTask)"来对该Task进行增强
 * TODO   暂时不知道原因add on 2021-06-17 by wenjing.liu
 *
 * TODO 然而放到一个非buildSrc下发现：
 * TODO 上述1和2的问题都不存在
 *
 * @author wenjing.liu
 */
public class TemplateTask extends DefaultTask {
    private String compileSdk;
    private File inputFile;
    //private TemplateEngine

    @Input
    @Optional
    public String getCompileSdk() {
        return compileSdk;
    }

    @InputFile
    @Optional
    public File getInputFile() {
        return inputFile;
    }

    @TaskAction
    public void taskAction() {
        SystemOutPrint.print("task action in Default Task");
    }
}

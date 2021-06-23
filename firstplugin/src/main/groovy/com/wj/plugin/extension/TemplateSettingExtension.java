package com.wj.plugin.extension;

import com.wj.plugin.SystemOutPrint;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;

import java.io.File;

/**
 * Created by wenjing.liu on 2021/6/21 in J1.
 * <p>
 * 用来配置项目的一些配置项
 *
 * @author wenjing.liu
 */
public class TemplateSettingExtension {
    public static final String TAG = "templateSettingExtension";

    private String compileSdk;
    private File interfaceSourceDir;

    public String getCompileSdk() {
        return compileSdk;
    }

    /**
     * 必须有setter方法,否则在build.gradle配置该项的时候会识别不到
     */

//    public void setCompileSdk(String sdk) {
//        this.compileSdk = sdk;
//        SystemOutPrint.println(" compileSdk = " + compileSdk);
//    }


//    public void setInterfaceSourceDir(File interfaceSourceDir) {
//        this.interfaceSourceDir = interfaceSourceDir;
//        SystemOutPrint.println(" interfaceSourceDir = " + interfaceSourceDir);
//    }

    public File getInterfaceSourceDir() {
        return interfaceSourceDir;
    }

}

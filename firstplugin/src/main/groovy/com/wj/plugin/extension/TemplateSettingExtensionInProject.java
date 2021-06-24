package com.wj.plugin.extension;

import java.io.File;

/**
 * Created by wenjing.liu on 2021/6/24 in J1.
 * <p>
 * 该类用来对本插件自身进行属性扩展，将该扩展添加到本插件的project中
 *
 * @author wenjing.liu
 */
public class TemplateSettingExtensionInProject {

    public static final String TAG = "templateSettingExtensionInProject";

    private String compileSdk;
    private File interfaceSourceDir;

    public String getCompileSdk() {
        return compileSdk;
    }

    public void setCompileSdk(String compileSdk) {
        this.compileSdk = compileSdk;
    }

    public File getInterfaceSourceDir() {
        return interfaceSourceDir;
    }

    public void setInterfaceSourceDir(File interfaceSourceDir) {
        this.interfaceSourceDir = interfaceSourceDir;
    }
}

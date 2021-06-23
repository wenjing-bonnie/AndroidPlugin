package com.wj.plugin.extension;

import java.io.File;
import java.util.Map;

/**
 * Created by wenjing.liu on 2021/6/21 in J1.
 * <p>
 * 用来配置项目的一些配置项:必须有对应的setter和getter方法，否则会提示read-only
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

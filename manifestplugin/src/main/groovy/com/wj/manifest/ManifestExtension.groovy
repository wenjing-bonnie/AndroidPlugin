package com.wj.manifest

import java.io.File;

/**
 * Created by wenjing.liu on 2021/9/18 in J1.
 * 扩展属性
 * 然后再用来学习下kotlin语法
 * @author wenjing.liu
 */
class ManifestExtension {
    protected static final String TAG = "ManifestPlugin"
    private File versionFile

    protected void setVersionFile(File file) {
        this.versionFile = file
    }

    protected File getVersionFile() {
        return versionFile
    }

}
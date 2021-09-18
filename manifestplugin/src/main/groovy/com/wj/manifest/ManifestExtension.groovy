package com.wj.manifest
/**
 * Created by wenjing.liu on 2021/9/18 in J1.
 * 扩展属性
 * 然后再用来学习下kotlin语法
 * @author wenjing.liu
 */
class ManifestExtension {
    protected static final String TAG = "ManifestPlugin"
    private File versionFile

    void setVersionFile(String file) {
        this.versionFile = file
    }

    String getVersionFile() {
        return versionFile
    }

}
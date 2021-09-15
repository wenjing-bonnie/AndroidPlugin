package com.wj.manifest;

import com.android.build.gradle.tasks.ProcessMultiApkApplicationManifest;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenjing.liu on 2021/9/9 in J1.
 * <p>
 * 适配Android12,为每个带有<intent-filter>添加android:exported="true"属性
 *
 * @author wenjing.liu
 */
public class AddExportForManifestTask extends DefaultTask {
    private String manifestFilePath;
    private List variantNames = new ArrayList<String>();
    protected static final String TAG = "AddExportForManifestTask";

    /**
     * 设置Manifest文件的路径
     *
     * @param path 如Users/j1/Documents/android/code/studio/AndroidPlugin/app/build/intermediates/merged_manifest/xiaomiRelease/AndroidManifest.xml
     */
    public void setManifestFilePath(String path) {
        this.manifestFilePath = path;
    }

    /**
     * 设置所有的变体名称
     *
     * @param names
     */
    public void setVariantNames(List names) {
        this.variantNames = names;
    }

    @TaskAction
    public void run() {
        SystemPrint.outPrintln(" AddExportForManifestTask is running !");
        handlerVariantsManifestFile();
    }

    /**
     * 处理所有变体的AndroidManifest文件
     */
    private void handlerVariantsManifestFile() {
        int size = variantNames.size();
        for (int i = 0; i < size; i++) {
            String deleteManifest = manifestFilePath.substring(0, manifestFilePath.lastIndexOf("/"));
            String newFilePath = String.format("%s/%s/AndroidManifest.xml",
                    deleteManifest.substring(0, deleteManifest.lastIndexOf("/")), variantNames.get(i));
            File manifestFile = new File(newFilePath);
            handlerVariantManifestFile(manifestFile);
        }
    }

    /**
     * 处理单个变体的Manifest文件
     */
    private void handlerVariantManifestFile(File manifestFile) {
        if (!manifestFile.exists()) {
            return;
        }
        SystemPrint.outPrintln("正在处理\n " + manifestFile.getAbsolutePath());
        
    }

}

package com.wj.manifest;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * Created by wenjing.liu on 2021/9/9 in J1.
 * <p>
 * 适配Android12,为每个带有<intent-filter>添加android:exported="true"属性
 *
 * @author wenjing.liu
 */
public class AddExportForManifestTask extends DefaultTask {
    protected static final String TAG = "AddExportForManifestTask";

    @TaskAction
    public void run() {
        SystemPrint.outPrintln(" AddExportForManifestTask is running !");
    }
}

package com.wj.manifest;

import org.gradle.api.DefaultTask;

/**
 * Created by wenjing.liu on 2021/9/9 in J1.
 * <p>
 * 适配Android12,为每个带有<intent-filter>添加android:exported="true"属性
 *
 * @author wenjing.liu
 */
class AddExportTask extends DefaultTask {
}

package com.wj.manifest.task

import com.wj.manifest.ManifestExtension
import com.wj.manifest.utils.SystemPrint
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by wenjing.liu on 2021/9/17 in J1.
 * 读取配置文件(含有对每个版本的信息描述)中的versionCode与versionName
 * 然后在加上360的自动打包流程
 * 用来复写下Extension的定义 以及@InputFile
 *
 *  调用 clean 以后第一次编译的过程，这个就是全量编译，
 *  之后修改了代码或者资源文件，再次编译，就是增量编译。
 * @author wenjing.liu
 */
class SetLastVersionTask extends DefaultTask {
    protected static final String TAG = "SetLastVersionTask"
    File manifestFile
    String versionName
    String versionCode
    /**
     * 设置Manifest文件的路径如Users/j1/Documents/android/code/studio/AndroidPlugin/app/build/intermediates/merged_manifest/xiaomiRelease/AndroidManifest.xml
     * @param file
     */
    void setManifestFile(File file) {
        this.manifestFile = file
    }

    @TaskAction
    void doTaskAction() {
        SystemPrint.outPrintln(String.format("Running handler the manifest is  \n %s ", manifestFile.getAbsolutePath()))
        ManifestExtension extension = project.getExtensions().findByType(ManifestExtension)
        File versionFile = extension.versionFile
        if (versionFile == null || !versionFile.exists()) {
            SystemPrint.errorPrintln("NO VERSION XML SOURCE")
            return
        }
        handlerVersionNameAndCodeForAndroidManifest(versionFile)
    }

    /**
     * 为Manifest文件添加新的版本号
     * @param versionFile
     */
    void handlerVersionNameAndCodeForAndroidManifest(File versionFile) {
        SystemPrint.outPrintln(String.format("Handler the versionFile is\n") + versionFile)
        readVersionCodeAndVersionName(versionFile)
        if (versionName == null || versionName.length() == 0 || versionCode == null || versionCode.length() == 0) {
            SystemPrint.errorPrintln("The config version file must set the <versionName> and <versionCode> !")
            return
        }
        writeVersionCodeAndVersionNameForManifest()
    }

    /**
     * 从versionFile中读取versionCode和versionName
     * @param versionFile
     */
    void readVersionCodeAndVersionName(File versionFile) {

        XmlParser xmlParser = new XmlParser()
        def node = xmlParser.parse(versionFile)

        node.children().find {
            if (hasNewVersionTagChildNode(it)) {
                handleVersionChildNode(it)
                return true
            }
        }
    }
    /**
     * 为Manifest文件写入配置的versionCode and versionName
     */
    void writeVersionCodeAndVersionNameForManifest() {
        SystemPrint.outPrintln(String.format("Set the new versionCode %s , versionName %s", versionCode, versionName))
        XmlParser xmlParser = new XmlParser()
        def node = xmlParser.parse(manifestFile)
        node.attributes().each {
            SystemPrint.outPrintln(it.key + " , " + it.value.toString())
        }

    }

    /**
     * 获取的xml中配置的versionCode和versionName
     * @param it
     */
    void handleVersionChildNode(Node it) {
        versionCode = it.versionCode.text()
        versionName = it.versionName.text()
    }

    /**
     * 含有latest="true"的节点
     * @param it
     * @return
     */
    boolean hasNewVersionTagChildNode(Node it) {
        boolean isFindNewVersion = false
        it.attributes().find {
            if (("latest").equals(it.key.toString())) {
                isFindNewVersion = it.value
                //找到标记" latest="true" "为最新版本的标记
                if (isFindNewVersion) {
                    return true
                }
            }
        }
        return isFindNewVersion
    }


/**  IncrementalTask  //全量的时候执行操作
 @Override
  protected void doFullTaskAction() throws Exception {}//增量的时候执行操作
 @Override
  protected void doIncrementalTaskAction(@NotNull Map<File, ? extends FileStatus> changedInputs) throws Exception {super.doIncrementalTaskAction(changedInputs)}

 @Override
  Property<AnalyticsService>                                                                          getAnalyticsService() {return null}

 @Override
  WorkerExecutor getWorkerExecutor() {return null}

 @Override
  protected boolean getIncremental() {//return true 支持增量; false:不支持增量
  //MergeResources：增量任务过程和全量其实差异不大，只不过是在获取 resourceSets 的时候，使用的是修改后的文件
  return super.getIncremental()} */
}
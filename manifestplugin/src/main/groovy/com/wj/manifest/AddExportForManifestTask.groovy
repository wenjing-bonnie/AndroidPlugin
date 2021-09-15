package com.wj.manifest


import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.xml.sax.SAXException

import javax.xml.parsers.ParserConfigurationException

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
    //private final String ATTRIBUTE_EXPORT = "{http://schemas.android.com/apk/res/android}exported"

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
        try {
            XmlParser xmlParser = new XmlParser();
            def node = xmlParser.parse(manifestFile);

            //SystemPrint.outPrintln("package = " + node.attributes().get("package"));
            //node.attributes();获取的一级内容<?xml> <manifest>里设置的内容如:key为package、encoding,value为对应的值
            //node.children();获取的二级内容 <application> <uses-sdk>
            //node.application直接可获取到<application>这级标签
            node.application.activity.each {
                //attributes()取得是在<activity >里面配置的属性值,而里面嵌套的<></>可直接通过.xxx的形式取得
                def attrs = it.attributes()
                //如果含有了android:exported,则直接返回.
                boolean isExported = hasAttributeExported(attrs);
                if (hasAttributeExported(attrs)) {
                    SystemPrint.outPrintln("已经含有了android:exported,直接返回")
                    //结束本次循环,相当于continue find return true相当于break
                    return true
                }
                //得到配置的<activity>里面的如<intent-filter>
                def children = it.children()
                if (hasIntentFilter(children)) {
                    handlerAddExportForActivity(it)
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace()
        } catch (SAXException e) {
            e.printStackTrace()
        } catch (IOException e) {
            e.printStackTrace()
        }
    }
    /**
     * 添加android:export
     */
    void handlerAddExportForActivity(Node activity) {
        SystemPrint.outPrintln(String.format("开始添加android:exported"))
        SystemPrint.errorPrintln("activity it = " + activity)

    }
    /**
     * 是否含有android:exported属性
     * TODO attrs.containsKey(ATTRIBUTE_EXPORT) 不起作用
     * @return
     */
    boolean hasAttributeExported(Map attrs) {
        String ATTRIBUTE_EXPORT = "{http://schemas.android.com/apk/res/android}exported"
        boolean isExported = false
        attrs.find {
            SystemPrint.outPrintln("hasAttributeExported key = " + it.key + " , value = " + it.value)
            if (ATTRIBUTE_EXPORT.equals(it.key.toString())) {
                isExported = true
                return true
            }
        }
        return isExported
    }
    /**
     * 是否含有<intent-filter>
     * @param children
     * @return
     */
    boolean hasIntentFilter(List children) {
        boolean isIntent = false
        children.find {
            SystemPrint.errorPrintln("hasIntentFilter children name = " + it.name())
            if ("intent-filter".equals(it.name())) {
                isIntent = true
                return true
            }
        }
        return isIntent
    }

}

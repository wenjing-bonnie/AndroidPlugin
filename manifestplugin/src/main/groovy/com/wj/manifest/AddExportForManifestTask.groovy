package com.wj.manifest;

import com.android.aapt.Resources;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import groovy.util.Node;
import groovy.util.XmlParser;

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
    private final String ATTRIBUTE_EXPORT = "{http://schemas.android.com/apk/res/android}exported"

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
        //String content = getManifestFileContent(manifestFile);
        //SystemPrint.outPrintln("内容为:\n" + content);
        try {
            XmlParser xmlParser = new XmlParser();
            def node = xmlParser.parse(manifestFile);

            SystemPrint.outPrintln("package = " + node.attributes().get("package"));
            //node.attributes();获取的一级内容<?xml> <manifest>里设置的内容如:key为package、encoding,value为对应的值
            //node.children();获取的二级内容 <application> <uses-sdk>
            //node.application直接可获取到<application>这级标签
            node.application.activity.each {
                SystemPrint.errorPrintln("activity it = " + it)
                //attributes()取得是在<activity >里面配置的属性值,而里面嵌套的<></>可直接通过.xxx的形式取得
                def attrs = it.attributes()
                //如果含有了android:exported,则直接返回
                String ATTRIBUTE_EXPORT = "{http://schemas.android.com/apk/res/android}exported"
//                if (true) {
//                    return
//                }

                attrs.each {
                    SystemPrint.errorPrintln("activity key = " + it.key + " , value = " + it.value)
                    if (attrs.containsKey("{http://schemas.android.com/apk/res/android}exported")) {
                        SystemPrint.outPrintln("containsKey")
                    }
                    if ("{http://schemas.android.com/apk/res/android}exported".equals(it.key)) {
                        SystemPrint.outPrintln("equals")
                    }
                }
                //SystemPrint.outPrintln("intent filter = " + it.children().getAt("intent-filter"))
                //得到配置的<activity>里面的如<intent-filter>
                def children = it.children()
                children.each {
                    SystemPrint.errorPrintln("activity children name = " + it.name())
                    if (!attrs.containsKey(ATTRIBUTE_EXPORT) && "intent-filter".equals(it.name)) {
                        handlerAddExportForActivity()
                    }
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

    void handlerAddExportForActivity() {

        SystemPrint.outPrintln("开始添加android:exported")

    }

    /**
     * 获取Manifest里面的内容
     *
     * @param manifestFile
     * @return
     */
    private String getManifestFileContent(File manifestFile) {
        StringBuffer contentBuffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(manifestFile));
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuffer.append(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuffer.toString();
    }

}

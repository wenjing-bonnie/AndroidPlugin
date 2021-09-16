package com.wj.manifest

import groovy.xml.XmlUtil
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
class AddExportForManifestTask extends DefaultTask {
    private String manifestFilePath;
    private List variantNames = new ArrayList<String>();
    protected static final String TAG = "AddExportForManifestTask";
    String ATTRIBUTE_EXPORT = "{http://schemas.android.com/apk/res/android}exported"

    /**
     * 设置Manifest文件的路径
     *
     * @param path 如Users/j1/Documents/android/code/studio/AndroidPlugin/app/build/intermediates/merged_manifest/xiaomiRelease/AndroidManifest.xml
     */
    void setManifestFilePath(String path) {
        this.manifestFilePath = path;
    }

    /**
     * 设置所有的变体名称
     *
     * @param names
     */
    void setVariantNames(List names) {
        this.variantNames = names;
    }

    @TaskAction
    void run() {
        SystemPrint.outPrintln(" AddExportForManifestTask is running !");
        //处理所有变体的AndroidManifest文件
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
            return
        }
        SystemPrint.outPrintln("正在处理\n " + manifestFile.getAbsolutePath());
        try {
            XmlParser xmlParser = new XmlParser();
            def node = xmlParser.parse(manifestFile);

            //SystemPrint.outPrintln("package = " + node.attributes().get("package"));
            //node.attributes();获取的一级内容<?xml> <manifest>里设置的内容如:key为package、encoding,value为对应的值
            //node.children();获取的二级内容 <application> <uses-sdk>
            //node.application直接可获取到<application>这级标签
            //第一步:处理<activity>
            node.application.activity.each {
                SystemPrint.outPrintln("The idada is = " + it)
                //如果已经有android:exported,则直接循环下一个:return true 相当于continue
                if (handlerEveryNodeWithoutExported(it)) {
                    return true
                }
            }
            //第二步:处理<service>
            //第三步:处理<receiver>
            //第四步:保存到原AndroidManifest文件中
            String result = XmlUtil.serialize(node)
            manifestFile.write(result, "utf-8")
        } catch (ParserConfigurationException e) {
            e.printStackTrace()
        } catch (SAXException e) {
            e.printStackTrace()
        } catch (IOException e) {
            e.printStackTrace()
        }
    }
    /**
     * 为每个需要添加android:exported的node
     * 在each{}中该方法不能定义为private,否则会提示找到该方法
     * @param it
     * @return
     */
    boolean handlerEveryNodeWithoutExported(Node it) {
        //attributes()取得是在<activity >里面配置的属性值,而里面嵌套的<></>可直接通过.xxx的形式取得
        def attrs = it.attributes()
        //如果含有了android:exported,则直接处理下一个.
        if (hasAttributeExported(attrs)) {
            SystemPrint.errorPrintln("已经含有了\" android:exported \",可以处理下一个了")
            //结束本次循环,相当于continue find return true相当于break
            return true
        }
        //得到配置的<activity>里面的如<intent-filter>
        def children = it.children()
        if (hasIntentFilter(children)) {
            SystemPrint.outPrintln(String.format("因为处理的第三方SDK,所以都添加\" android:exported=true \""))
            handlerAddExportForNode(it)
        }
        return false
    }
    /**
     * 添加android:export
     */
    private void handlerAddExportForNode(Node node) {
        SystemPrint.outPrintln("The node is = " + node)
        SystemPrint.outPrintln(" ==== ")
        //putAt()只能给已有的属性进行修改value
        node.attributes().replace("android:name","1")
        //注意这里使用的是"android:exported"而不是ATTRIBUTE_EXPORT!!!!!!
        node.attributes().put("android:exported", true)
    }
    /**
     * 是否含有android:exported属性
     * TODO attrs.containsKey(ATTRIBUTE_EXPORT) 不起作用
     * @return
     */
    private boolean hasAttributeExported(Map attrs) {
        boolean isExported = false
        attrs.find {
            //SystemPrint.outPrintln("hasAttributeExported key = " + it.key + " , value = " + it.value)
            if (ATTRIBUTE_EXPORT.equals(it.key.toString())) {
                isExported = true
                //find return true相当于break
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
    private boolean hasIntentFilter(List children) {
        boolean isIntent = false
        children.find {
            //SystemPrint.errorPrintln("hasIntentFilter children name = " + it.name())
            if ("intent-filter".equals(it.name())) {
                isIntent = true
                //find return true相当于break
                return true
            }
        }
        return isIntent
    }

}

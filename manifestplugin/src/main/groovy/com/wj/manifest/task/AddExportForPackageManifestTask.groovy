package com.wj.manifest.task

import com.wj.manifest.utils.SystemPrint
import groovy.xml.XmlUtil
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction
import org.xml.sax.SAXException

import javax.xml.parsers.ParserConfigurationException

/**
 * Created by wenjing.liu on 2021/9/16 in J1.
 * <p>
 * 适配Android12,为每个带有<intent-filter>添加android:exported="true"属性
 * 在合并所有的Manifest之前为所有的AndroidManifest文件添加
 * @author wenjing.liu
 */
class AddExportForPackageManifestTask extends DefaultTask {
    protected static String TAG = "AddExportForPackageManifestFromManifestProject"
    String ATTRIBUTE_EXPORT = "{http://schemas.android.com/apk/res/android}exported"
    private FileCollection manifestCollection
    private File mainManifestFile
    private boolean isMainManifestFile

    /**
     * 设置所有的 需要合并的Manifest文件
     * @param collection
     */
    void setManifestsFileCollection(FileCollection collection) {
        manifestCollection = collection
    }

    /**
     *
     * @param file
     */
    void setMainManifestFile(File file) {
        mainManifestFile = file
    }

    @TaskAction
    void doTaskAction() {
        //处理所有包下的AndroidManifest文件添加android:exported
        SystemPrint.outPrintln(TAG, "Running .....")
        isMainManifestFile = false
        manifestCollection.each {
            handlerVariantManifestFile(it)
        }
        //自己APP中的manifest文件只提示增加,不主动添加
        isMainManifestFile = true
        handlerVariantManifestFile(mainManifestFile)
    }

    /**
     * 处理单个变体的Manifest文件
     */
    void handlerVariantManifestFile(File manifestFile) {
        if (!manifestFile.exists()) {
            return
        }
        def node = readManifestFromPackageManifest(manifestFile)
        writeManifestForPackageManifest(manifestFile, node)
    }

    /**
     * 读manifest内容
     * @param manifestFile
     * @return
     */
    Node readManifestFromPackageManifest(File manifestFile) {
        try {
            XmlParser xmlParser = new XmlParser()
            def node = xmlParser.parse(manifestFile)

            //node.attributes();获取的一级内容<?xml> <manifest>里设置的内容如:key为package、encoding,value为对应的值
            //node.children();获取的二级内容 <application> <uses-sdk>
            //node.application直接可获取到<application>这级标签
            //第一步:处理<activity>
            node.application.activity.each {
                //如果已经有android:exported,则直接循环下一个:return true 相当于continue
                if (handlerEveryNodeWithoutExported(it)) {
                    return true
                }
            }
            //第二步:处理<service>
            node.application.service.each {
                //如果已经有android:exported,则直接循环下一个:return true 相当于continue
                if (handlerEveryNodeWithoutExported(it)) {
                    return true
                }
            }
            //第三步:处理<receiver>
            node.application.receiver.each {
                //如果已经有android:exported,则直接循环下一个:return true 相当于continue
                if (handlerEveryNodeWithoutExported(it)) {
                    return true
                }
            }
            return node

        } catch (ParserConfigurationException e) {
            e.printStackTrace()
        } catch (SAXException e) {
            e.printStackTrace()
        } catch (IOException e) {
            e.printStackTrace()
        }
    }

/**
 * 第四步:保存到原AndroidManifest文件中
 * @param manifestFile
 * @param node
 */
    void writeManifestForPackageManifest(File manifestFile, Node node) {
        if (isMainManifestFile) {
            //如果是主module的manifest,自行添加
            return
        }
        String result = XmlUtil.serialize(node)
        manifestFile.write(result, "utf-8")
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
            //SystemPrint.errorPrintln(TAG, String.format("The \" %s \" already has \" android:exported \" , to next one .", it.name()))
            //结束本次循环,相当于continue find return true相当于break
            return true
        }
        //得到配置的<activity>里面的如<intent-filter>
        def children = it.children()
        if (hasIntentFilter(children)) {
            handlerAddExportForNode(it)
        }
        return false
    }
    /**
     * 添加android:export
     */
    private void handlerAddExportForNode(Node node) {
        if (isMainManifestFile) {
            //仅做提示
            String errorFormat = "To solve the build error \n \"Apps targeting Android 12 and higher are required to specify an explicit value for `android:exported` when the corresponding component has an intent filter defined\"\n" +
                    "you must set \"android:exported\" based on actual demand for manifest in main module of \n \" %s \""
            SystemPrint.errorPrintln(TAG, String.format(errorFormat, node.attributes().toString()))
            return
        }
        SystemPrint.outPrintln(TAG, String.format("Handler third sdk of \"%s\" , so add \"android:exported=true\" .", node.name()))
        SystemPrint.outPrintln(TAG, String.format("In Handler:  \n %s", node.attributes()))
        //注意这里使用的是"android:exported"而不是ATTRIBUTE_EXPORT!!!!!!
        node.attributes().put("android:exported", true)
        //node.attributes().put(ATTRIBUTE_EXPORT,"true")
        //TODO 该种方式就可以替换,但是之前已有的不管采用{http://schemas.android.com/apk/res/android}name还是android:name都无法赋值成功
        /**这个原因跟在hasAttributeExported()使用attrs.containsKey(ATTRIBUTE_EXPORT)是一个原因,
         * 只能在attrs.each中取出里面key在进行判断才可以返回true,然后在调用下面的方法才可以替换成功
         * node.attributes().replace(new String("android:name"), "add")
         * node.attributes().replace(new String("{http://schemas.android.com/apk/res/android}name"), "ddd")
         * */
    }


    /**
     * 是否含有android:exported属性
     * TODO attrs.containsKey(ATTRIBUTE_EXPORT) 不起作用
     * @return
     */
    private boolean hasAttributeExported(Map attrs) {
        boolean isExported = false
        attrs.find {
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
            if ("intent-filter".equals(it.name())) {
                isIntent = true
                //find return true相当于break
                return true
            }
        }
        return isIntent
    }


}

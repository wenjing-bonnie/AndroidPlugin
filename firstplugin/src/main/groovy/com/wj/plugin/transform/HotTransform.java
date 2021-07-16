package com.wj.plugin.transform;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;
import com.wj.plugin.SystemOutPrint;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Created by wenjing.liu on 2021/7/6 in J1.
 * 学习有class到dex的Transform过程
 * <p>
 * 每个Transform都是一个gradle task，将class、本地jar、aar和resource资源统一处理
 * 每个Transform在处理之后交给下一个Transform。如果是自定义的Transform会插在队列最前面
 *
 * @author wenjing.liu
 */
public class HotTransform extends Transform {

    @Override
    public String getName() {
        return "HotTransformTask";
    }

    /**
     * 指定需要处理的数据.
     * CONTENT_CLASS表示处理的是java class文件
     * CONTENT_RESOURCES：处理的是java资源文件
     *
     * @return
     */
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    /**
     * 需要操作的内容范围
     *
     * @return
     */
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        //仅仅用来查看input文件
        //return TransformManager.EMPTY_SCOPES;
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    /**
     * 仅仅用来设置查看input文件的作用域
     */
//    @Override
//    public Set<? super QualifiedContent.Scope> getReferencedScopes() {
//        return TransformManager.SCOPE_FULL_PROJECT;
//    }

    /**
     * 是否增量编译
     *
     * @return
     */
    @Override
    public boolean isIncremental() {
        return false;
    }

    /**
     * 输出内容
     *
     * @param transformInvocation
     * @throws TransformException
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        //如果不带super，就不会生成dex文件
        super.transform(transformInvocation);

        SystemOutPrint.println("context  project name = " + transformInvocation.getContext().getProjectName()
                + "context  project name = " + transformInvocation.getContext().getPath()
                + " , isIncremental = " + transformInvocation.isIncremental());
        //现在进行处理.class文件：消费型输入，需要输出给下一个任务
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        //仅仅用来查看input文件：引用型输入，无需输出，此时outputProvider为null
        //Collection<TransformInput> inputs = transformInvocation.getReferencedInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        for (TransformInput input : inputs) {
            //返回的是ImmutableJarInput。
            for (JarInput jar : input.getJarInputs()) {
                SystemOutPrint.println("jar file = " + jar.getFile());
                //TODO 在这里增加处理.jar文件的代码

                //获取Transforms的输出目录
                File dest = outputProvider.getContentLocation(jar.getFile().getAbsolutePath(), jar.getContentTypes(), jar.getScopes(), Format.JAR);
                //将修改之后的文件拷贝到对应outputProvider的目录中
                FileUtils.copyFile(jar.getFile(), dest);
            }
            //返回的是ImmutableDirectoryInput
            for (DirectoryInput directory : input.getDirectoryInputs()) {
                SystemOutPrint.println("directory file = " + directory.getFile());
                //TODO 在这里增加处理.class文件的代码

                //获取Transforms的输出目录
                File dest = outputProvider.getContentLocation(directory.getName(), directory.getContentTypes(), directory.getScopes(), Format.DIRECTORY);
                //将修改之后的文件拷贝到对应outputProvider的目录中
                FileUtils.copyDirectory(directory.getFile(), dest);
            }
        }
    }

}

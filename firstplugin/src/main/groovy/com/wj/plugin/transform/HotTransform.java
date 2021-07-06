package com.wj.plugin.transform;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.wj.plugin.SystemOutPrint;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
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
        return "Hot transform task";
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
     * PROJECT:只有项目目录
     * SUB_PROJECT:只有子项目目录
     * EXTERNAL_LIBRARIES:只有外部库
     * TESTED_CODE:当前变量（包括依赖项)测试的代码
     * PROVIDED_ONLY:本地或者员村依赖项
     *
     * @return
     */
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

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
        // super.transform(transformInvocation);
        SystemOutPrint.println("context  project name = " + transformInvocation.getContext().getProjectName()
                + "context  project name = " + transformInvocation.getContext().getPath()
                + " , isIncremental = " + transformInvocation.isIncremental());
        SystemOutPrint.println(" isIncremental = " + transformInvocation.isIncremental());
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        for (TransformInput input : inputs) {
            //返回的是ImmutableJarInput
            for (JarInput jar : input.getJarInputs()) {
                SystemOutPrint.println("jar file = " + jar.getFile());
            }
            //返回的是ImmutableDirectoryInput
            for (DirectoryInput directory : input.getDirectoryInputs()) {
                SystemOutPrint.println("directory file = " + directory.getFile());
            }
        }
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        SystemOutPrint.println("output  = " + outputProvider);
    }
}

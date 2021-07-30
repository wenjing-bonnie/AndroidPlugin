package com.android.wj.debug.transform;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.wj.debug.utils.SystemOutPrintln;

import org.gradle.internal.impldep.org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Created by wenjing.liu on 2021/7/19 in J1.
 *
 * @author wenjing.liu
 */
public class LeakCanaryTransform extends Transform {

    /**
     * 设置该Task的名字
     *
     * @return
     */
    @Override
    public String getName() {
        return String.format("%sTask", getClass().getSimpleName());
    }

    /**
     * 设置输入文件类型
     *
     * @return
     */
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    /**
     * 设置输入文件的作用域
     *
     * @return
     */
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        //return TransformManager.SCOPE_FULL_PROJECT;
        return TransformManager.EMPTY_SCOPES;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getReferencedScopes() {
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


    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        Collection<TransformInput> transformInputs = transformInvocation.getReferencedInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        for (TransformInput input : transformInputs) {
            for (JarInput jar : input.getJarInputs()) {
              //  SystemOutPrintln.println("jar name = " + jar.getName());
            }
            for (DirectoryInput directory : input.getDirectoryInputs()) {
                SystemOutPrintln.println("directory name = " + directory.getName());

                handleDirectoryInput(directory, outputProvider);
            }
        }
    }

    /**
     * 找到Application，然后添加代码
     *
     * @param directoryInput
     * @param outputProvider
     */
    private void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        File file = directoryInput.getFile();
        SystemOutPrintln.println("file path = "+file.getPath());
        if (directoryInput.getFile().isDirectory()) {

        }
    }
}

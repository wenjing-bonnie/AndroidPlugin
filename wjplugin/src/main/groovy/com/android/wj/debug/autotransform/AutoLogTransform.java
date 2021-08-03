package com.android.wj.debug.autotransform;

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
import com.android.wj.debug.autologvisitor.AutoLogClassVisitor;
import com.android.wj.debug.utils.SystemOutPrintln;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Created by wenjing.liu on 2021/7/19 in J1.
 *
 * @author wenjing.liu
 */
public class AutoLogTransform extends Transform {

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
        return TransformManager.SCOPE_FULL_PROJECT;
        //return TransformManager.EMPTY_SCOPES;
    }

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


    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        Collection<TransformInput> transformInputs = transformInvocation.getInputs();
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
        File directorFile = directoryInput.getFile();
        SystemOutPrintln.println("file path = " + directorFile.getPath());
        File outputFile = outputProvider.getContentLocation(directoryInput.getName(), directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);
        handleFileFromDirectory(directorFile, outputFile);
        try {
            FileUtils.copyDirectory(directoryInput.getFile(), outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到里面的file,进行处理file
     *
     * @param input
     */
    private void handleFileFromDirectory(File input, File output) {
        if (!input.isDirectory()) {
            addLogForClass(input.getAbsolutePath(), output.getAbsolutePath());
            return;
        }
        for (File file : input.listFiles()) {
            handleFileFromDirectory(file, output);
        }

    }

    /**
     * 为每个class文件的方法添加日志文件
     *
     * @param input
     * @param output
     */
    private void addLogForClass(String input, String output) {
        if (input == null || output == null) {
            return;
        }
        SystemOutPrintln.println("input = " + input + " , output = " + output);
        try {
            FileInputStream is = new FileInputStream(input);
            ClassReader reader = new ClassReader(is);
            ClassWriter writer = new ClassWriter(reader,ClassWriter.COMPUTE_MAXS);
            AutoLogClassVisitor classVisitor = new AutoLogClassVisitor(writer);
            reader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
            FileOutputStream fos = new FileOutputStream(output);
            fos.write(writer.toByteArray());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

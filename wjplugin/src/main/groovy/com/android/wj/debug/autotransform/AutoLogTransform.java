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
import org.objectweb.asm.ClassVisitor;
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
 * <p>
 * 为所有的方法添加日志
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
        //TODO 根据是否需要增量编译，做下处理

        Collection<TransformInput> transformInputs = transformInvocation.getInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        for (TransformInput input : transformInputs) {
            //jar包不做处理,原样复制
            for (JarInput jar : input.getJarInputs()) {
                //获取Transforms的输出目录
                File dest = outputProvider.getContentLocation(jar.getFile().getAbsolutePath(), jar.getContentTypes(), jar.getScopes(), Format.JAR);
                //将修改之后的文件拷贝到对应outputProvider的目录中
                FileUtils.copyFile(jar.getFile(), dest);
            }
            //处理class文件
            for (DirectoryInput directory : input.getDirectoryInputs()) {
                //获取Transforms的输出目录
                File dest = outputProvider.getContentLocation(directory.getName(), directory.getContentTypes(), directory.getScopes(), Format.DIRECTORY);
                handleDirectoryInput(directory);
                //将修改之后的文件拷贝到对应outputProvider的目录中
                FileUtils.copyDirectory(directory.getFile(), dest);
            }
        }
    }

    /**
     * 找到Application，然后添加代码
     *
     * @param directoryInput
     */
    private void handleDirectoryInput(DirectoryInput directoryInput) {
        File directorFile = directoryInput.getFile();
        SystemOutPrintln.println("Handle the directory path =  " + directorFile.getPath());
        handleFileInDirectory(directorFile);
    }

    /**
     * 遍历里面的file,进行处理file
     *
     * @param input
     */
    private void handleFileInDirectory(File input) {
        if (!input.isDirectory()) {
            addLogForClass(input.getAbsolutePath(), input.getAbsolutePath());
            return;
        }
        for (File file : input.listFiles()) {
            handleFileInDirectory(file);
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
        SystemOutPrintln.println(String.format("Add log for \" %s \"", input));
        try {
            FileInputStream is = new FileInputStream(input);
            ClassReader reader = new ClassReader(is);
            /***
             * @param reader ClassReader
             * @param flags:
             *             0:不自动计算操作数栈和局部变量表的大小,需要手动指定
             *             COMPUTE_MAXS:自动计算操作数栈和局部变量表的大小,但必须手动触发
             *             COMPUTE_FRAMES:不仅自动计算操作数栈和局部变量表的大小,还会自动计算StackMapFrames
             *
             */
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
            //将文件进行增加log
            AutoLogClassVisitor classVisitor = new AutoLogClassVisitor(writer);

            /**
             * @param classVisitor:给定具体处理逻辑的ClassVisitor,通常需要自定义类来继承抽象类ClassVisitor.
             * @param parsingOptions:解析.class文件的选项.其中有几个取值:
             *                  SKIP_CODE:跳过方法体的code属性,比如方法字节码、异常表等;
             *                  SKIP_DEBUG:跳过文件中的调试信息,比如行号等;下面的这些方法不会被调用到 {@link ClassVisitor#visitSource}, {@link MethodVisitor#visitLocalVariable}, {@link MethodVisitor#visitLineNumber} and {@link MethodVisitor#visitParameter}
             *                  SKIP_FRAMES:跳过文件StackMapTable和StackMap属性;当ClassWriter设置 {@link ClassWriter#COMPUTE_FRAMES}才会起作用
             *                  EXPAND_FRAMES:跳过文件的StackMapTable属性*/
            reader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
            //然后将内容重新写入该文件中
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

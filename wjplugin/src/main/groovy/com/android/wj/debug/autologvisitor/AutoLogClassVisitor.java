package com.android.wj.debug.autologvisitor;

import com.android.wj.debug.utils.SystemOutPrintln;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Created by wenjing.liu on 2021/8/3 in J1.
 * <p>
 * 访问所有.class的类成员
 *
 * @author wenjing.liu
 */
public class AutoLogClassVisitor extends ClassVisitor {

    public AutoLogClassVisitor(ClassVisitor visitor) {
        // ASM API versions.  由于AdviceAdapter目前只支持到ASM7,所以这里只能设置Opcodes.ASM7,有时候会抛出(不是必现，还没有得出规律)
        //设置成Opcodes.ASM9的时候,有时候会出现"com.android.tools.r8.errors.b: Absent Code attribute in method that is not native or abstract"异常,改成ASM7即没有,暂时还没有找到原因
        super(Opcodes.ASM9, visitor);
    }

    /**
     * 当开始扫描类的时候回调的第一个方法
     *
     * @param version    jdk版本.如52则为jdk1.8,对应{@link Opcodes#V18}; 51则为jdk1.7,对应Opcodes的V17.具体参数对应值在 {@link Opcodes}中查看.
     * @param access     类修饰符.在ASM中以“ACC_”开头的常量:如 {@link Opcodes#ACC_PUBLIC}对应的public.具体参数对应值在 {@link Opcodes}中查看.
     * @param name       类名:包名+类名
     * @param signature  泛型信息，若未定义任何类型，则为空
     * @param superName  父类
     * @param interfaces 实现的接口的数组
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        SystemOutPrintln.println("visit version = " + version + " , access = " + access + " , name = " + name + " , signature = " + signature);
    }

    /**
     * @param source 访问的源文件
     * @param debug
     */
    @Override
    public void visitSource(String source, String debug) {
        super.visitSource(source, debug);
        SystemOutPrintln.println("visitSource source =" + source + " , debug = " + debug);
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        super.visitOuterClass(owner, name, descriptor);
        SystemOutPrintln.println("visitOuterClass owner =" + owner + " , name = " + name);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(name, outerName, innerName, access);
        SystemOutPrintln.println("visitOuterClass name =" + name + " , outerName = " + outerName + " , innerName = " + innerName);

    }

    /**
     * 扫描到类的方法回调该方法
     *
     * @param access     方法修饰符,同visit()中的access
     * @param name       方法名
     * @param descriptor 方法签名：(参数列表)返回类型,如void onCreate(Bundle savedInstanceState),返回的为(Landroid/os/Bundle;)V
     *                   I代表int;B代表byte;C代表char;D代表double;F代表float;J代表long;S代表short;Z代表boolean;V代表void;
     *                   [...;代表一维数组;[[...;代表二维数组;[[[...;代表三维数组
     *                   例如输入参数为:
     *                   1.String[]则返回[Ljava/lang/String;
     *                   2.int,String,int[]则返回(ILjava/lang/String;[I)
     * @param signature  泛型相关信息
     * @param exceptions 会抛出异常
     * @return MethodVisitor
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        SystemOutPrintln.println("visitMethod access = " + access + " , name = " + name + " , descriptor = " + descriptor);
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        //TODO 这里传入ASM9为什么没有报错？
        AdviceAdapter logAdviceAdapter = new AutoLogAdviceAdapter(this.api, methodVisitor, access, name, descriptor);
        return logAdviceAdapter;
        // return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    /**
     * 扫描到类中的成员变量时回调该方法
     *
     * @param access     字段的修饰符
     * @param name       字段的名字
     * @param descriptor 同visitMethod()的descriptor
     * @param signature  泛型相关的信息
     * @param value      默认值
     * @return
     */
    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        SystemOutPrintln.println("visitField descriptor = " + descriptor + " , name = " + name);
        return super.visitField(access, name, descriptor, signature, value);
    }

    /**
     * 扫描到类注解时回调该方法
     *
     * @param descriptor 注解类型
     * @param visible    在JVM是是否可见
     * @return AnnotationVisitor
     */
    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        SystemOutPrintln.println("visitAnnotation descriptor = " + descriptor + " , visible = " + visible);
        return super.visitAnnotation(descriptor, visible);
    }

    /**
     * 该类扫描结束才会调用,用于在该类中追加方法
     */

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}

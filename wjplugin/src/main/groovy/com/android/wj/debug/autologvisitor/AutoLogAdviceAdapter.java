package com.android.wj.debug.autologvisitor;

import com.android.wj.debug.utils.SystemOutPrintln;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Created by wenjing.liu on 2021/8/3 in J1.
 * 查看.class文件的所有方法,为所有的方法添加日志
 *
 * @author wenjing.liu
 */
public class AutoLogAdviceAdapter extends AdviceAdapter {
    private MethodVisitor methodVisitor;
    private String name;
    /**
     * 该方法的参数
     */
    private Type[] args;
    /**
     * 是否开启统计调用时间
     */
    private boolean isInjectCallTime = true;

    private Label labelBegin;
    private Label labelEnd;

    /**
     * Constructs a new {@link AdviceAdapter}.
     *
     * @param api           the ASM API version implemented by this visitor. Must be one of {@link
     *                      Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6} or {@link Opcodes#ASM7}.
     * @param methodVisitor the method visitor to which this adapter delegates calls.
     * @param access        the method's access flags (see {@link Opcodes}).
     * @param name          the method's name.
     * @param descriptor    the method's descriptor (see {@link Type Type}).
     */
    protected AutoLogAdviceAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
        this.name = name;
        this.methodVisitor = methodVisitor;
        this.args = Type.getArgumentTypes(descriptor);
        for (Type type : args) {
            SystemOutPrintln.println("internal name = " + type.getInternalName() + " , classname = " + type.getClassName());
        }
    }

    @Override
    public void visitParameter(String name, int access) {
        super.visitParameter(name, access);
        SystemOutPrintln.println(" = visitParameter name = " + name);
    }

    /**
     * 开始扫描该方法时回调该方法
     */
    @Override
    public void visitCode() {
        super.visitCode();
        SystemOutPrintln.println(" = visitCode");
    }

    /**
     * 进入到该方法的时候回调该方法
     */
    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        SystemOutPrintln.println(" = onMethodEnter");
        //methodVisitor.
        if (!isInjectCallTime()) {
            return;
        }
        //添加方法调用前的时间,对应代码:long beginTime = System.currentTimeMillis();
        systemCurrentTimeMillisOnMethodEnter();
    }


    /**
     * 添加方法调用前的时间,对应代码:long beginTime = System.currentTimeMillis();
     */
    private void systemCurrentTimeMillisOnMethodEnter() {
        labelBegin = new Label();
        methodVisitor.visitLabel(labelBegin);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        methodVisitor.visitVarInsn(Opcodes.LSTORE, 3);
    }

    /**
     * 之所以不在{@link #onMethodExit(int)}中,
     * 是因为如果源码的return带有运算表达式,会先调用{@link #onMethodExit(int)},然后在计算表达式,最后才调用{@link #visitInsn(int)}
     *
     * @param opcode
     */
    @Override
    public void visitInsn(int opcode) {
        if (!isInjectCallTime() || opcode < IRETURN || opcode > RETURN) {
            super.visitInsn(opcode);
            return;
        }
        //方法返回的时候,添加执行时间
        SystemOutPrintln.println(" = onMethodExit");
        //添加方法调用之后的时间,对应代码:long callTime = System.currentTimeMillis() - beginTime;
        systemCurrentTimeMillisOnMethodExit();

        //初级:输出方法执行时间,对应代码:Log.d("AUTO", String.valueOf(callTime));
        //logMethodExit();
        //中级:输出方法执行时间,对应代码:Log.d("AUTO", String.valueOf(callTime));
        logStringMethodExit();

        // visitLocalVariable()描述或定义存储在Code属性的LocalVariableTable和LocalVariableTypeTable属性中的调试信息。 它们不是正常操作所必需的，与StackMapTable存储的信息不同。
        // 换句话说，除非您想提供调试信息，否则无论是否自动计算堆栈映射帧，您都不需要调用visitLocalVariable() 。
        //请注意这些属性中存储的信息的差异。 LocalVariable[Type]Table存储有关源级语言的局部变量的名称和[泛型]类型及其范围。 StackMapTable存储有关字节码验证器的JVM类型系统的局部变量和操作数堆栈条目的类型信息。
        //methodVisitor.visitLocalVariable();
        super.visitInsn(opcode);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        SystemOutPrintln.println(" = visitEnd");
    }

    /**
     * 添加方法调用之后的时间,对应代码:long callTime = System.currentTimeMillis() - beginTime;
     */
    private void systemCurrentTimeMillisOnMethodExit() {
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        methodVisitor.visitVarInsn(LLOAD, 3);
        methodVisitor.visitInsn(LSUB);
        methodVisitor.visitVarInsn(LSTORE, 5);
    }

    /**
     * 输出方法执行时间,对应代码:Log.d("AUTO", String.valueOf(callTime));
     */
    private void logMethodExit() {
        //输出方法执行时间,对应代码:Log.d("AUTO", String.valueOf(callTime));
        methodVisitor.visitLdcInsn("AUTO");
        methodVisitor.visitVarInsn(LLOAD, 5);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(J)Ljava/lang/String;", false);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        methodVisitor.visitInsn(POP);
    }

    /**
     * 输出方法执行时间,对应代码:Log.d("AUTO", String.format("cost time is [%l]ms", callTime));
     */
    private void logStringMethodExit() {
        methodVisitor.visitLdcInsn("AUTO");
        String stringFormat = "[" + name + "] cost time is [%d] ms;";
        methodVisitor.visitLdcInsn(stringFormat);
        methodVisitor.visitInsn(ICONST_1);
        //创建数组
        methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Object");
        //复制栈顶
        methodVisitor.visitInsn(DUP);
        //将常量压入栈
        methodVisitor.visitInsn(ICONST_0);
        methodVisitor.visitVarInsn(LLOAD, 5);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
        methodVisitor.visitInsn(AASTORE);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/String", "format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        methodVisitor.visitInsn(POP);
        labelEnd = new Label();
        methodVisitor.visitLabel(labelEnd);
        methodVisitor.visitLocalVariable("beginTime", "J", null, labelBegin, labelEnd, 3);
        //当设置了ClassWriter.COMPUTE_MAXS的时候，会自动的计算visitMaxs(int maxStack, int maxLocals)
        //methodVisitor.visitMaxs(0,0);
    }


    private void logStringTagMethodExit() {

    }

    /**
     * 需要增加方法执行时间的标识
     *
     * @return
     */
    private boolean isInjectCallTime() {
        return !isInjectCallTime || !"<clinit>".equals(name);
    }

}

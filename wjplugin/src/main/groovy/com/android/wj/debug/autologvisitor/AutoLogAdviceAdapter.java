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
        this.methodVisitor = methodVisitor;
    }


    /**
     * 开始扫描该方法时回调该方法
     */
    @Override
    public void visitCode() {
        super.visitCode();
        SystemOutPrintln.println(" = visitCode");
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
        SystemOutPrintln.println(" = visitLocalVariable name = " + name);
    }

//    @Override
//    public void visitLineNumber(int line, Label start) {
//        super.visitLineNumber(line, start);
//        SystemOutPrintln.println(" = visitLineNumber line =  " + line + " , start = " + start.toString());
//    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        super.visitIntInsn(opcode, operand);
        SystemOutPrintln.println(" = visitIntInsn opcode = " + opcode);
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
        SystemOutPrintln.println(" = visitInsn opcode = " + opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
        SystemOutPrintln.println(" = visitMaxs maxStack = " + maxStack + " , maxLocals = " + maxLocals);
    }

//    @Override
//    public void visitLabel(Label label) {
//        super.visitLabel(label);
//        SystemOutPrintln.println(" = visitLabel label.info = " + label.toString());
//    }

    /**
     * 进入到该方法的时候回调该方法
     */
    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        SystemOutPrintln.println(" = onMethodEnter");
        //methodVisitor.
    }

    /**
     * 即将从该方法出去的时候回调该方法
     *
     * @param opcode
     */
    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        SystemOutPrintln.println(" = onMethodExit");
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        SystemOutPrintln.println(" = visitEnd");
    }

    /***
     * 访问该方法的传入参数
     * @param name
     * @param access
     */

    @Override
    public void visitParameter(String name, int access) {
        super.visitParameter(name, access);
        SystemOutPrintln.println(" = visitParameter name = " + name);
    }

    /**
     * 基于LDC、LDC_W、LDC2_W将一个常见加载到操作栈
     *
     * @param value
     */
    @Override
    public void visitLdcInsn(Object value) {
        super.visitLdcInsn(value);
        SystemOutPrintln.println(" = visitLdcInsn = ");
    }

    /**
     * 访问属性的操作指令，用来加载或存储Field
     *
     * @param opcode:访问属性的操作指令。如{@link Opcodes#PUTFIELD} {@link Opcodes#GETSTATIC}
     * @param owner
     * @param name
     * @param descriptor
     */
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        super.visitFieldInsn(opcode, owner, name, descriptor);
        SystemOutPrintln.println(" = visitFieldInsn = " + opcode + " , owner = " + owner + " , name = " + name);
    }


    /**
     * 访问局部变量的操作指令 Opcodes操作数栈对应的指令
     *
     * @param opcode:访问局部变量的操作指令.如{@link  Opcodes#ILOAD}、{@link Opcodes#ISTORE}、{@link Opcodes#RET}
     * @param var:访问的指令的操作数。即局部变量的索引（TODO 应该是常量池的那个索引）
     */
    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
        SystemOutPrintln.println(" = visitVarInsn opcode = " + opcode + " , var = " + var);
    }

    /**
     * 访问方法的操作指令
     *
     * @param opcode:访问方法的指令字节码。如{@link Opcodes#INVOKEVIRTUAL}{@link Opcodes#INVOKESPECIAL}
     *                                  {@link Opcodes#INVOKESTATIC} {@link Opcodes#INVOKEINTERFACE}
     * @param owner:方法的所有者类的内部名称        {@link Type#getInternalName()}
     * @param name
     * @param descriptor
     * @param isInterface
     */
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        SystemOutPrintln.println(" = visitMethodInsn opcode = " + opcode + " , owner = " + owner + " , name = " + name + " , descriptor = " + descriptor);
    }
}

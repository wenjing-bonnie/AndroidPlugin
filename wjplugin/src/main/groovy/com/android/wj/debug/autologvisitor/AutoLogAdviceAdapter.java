package com.android.wj.debug.autologvisitor;

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
    }

    /**
     * 开始扫描该方法时回调该方法
     */
    @Override
    public void visitCode() {
        super.visitCode();
    }

    /**
     * 进入到该方法的时候回调该方法
     */
    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
    }

    /**
     * 即将从该方法出去的时候回调该方法
     * @param opcode
     */
    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
    }

}

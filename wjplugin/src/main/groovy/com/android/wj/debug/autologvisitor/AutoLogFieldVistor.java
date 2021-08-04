package com.android.wj.debug.autologvisitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;

/**
 * Created by wenjing.liu on 2021/8/4 in J1.
 * <p>
 * 访问类的成员变量.有其访问顺序:{@link #visitAnnotation(String, boolean)}->{@link #visitTypeAnnotation(int, TypePath, String, boolean)}
 * ->{@link #visitAttribute(Attribute)}->{@link #visitEnd()}
 *
 * @author wenjing.liu
 */
public class AutoLogFieldVistor extends FieldVisitor {
    public AutoLogFieldVistor(int api, FieldVisitor fieldVisitor) {
        super(api, fieldVisitor);
    }

    /**
     * 访问成员变量的注解的时回调该方法
     *
     * @param descriptor
     * @param visible    在运行的时候是否可见
     * @return
     */
    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible);
    }

    /**
     * 访问具体的成员变量的时回调该方法
     *
     * @param attribute
     */
    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
    }

    /**
     * 访问具体的成员变量的类型上的注解时回调该方法
     *
     * @param typeRef
     * @param typePath
     * @param descriptor
     * @param visible
     * @return
     */
    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    /**
     * 访问完成员变量的时候必须回调该方法
     */
    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}

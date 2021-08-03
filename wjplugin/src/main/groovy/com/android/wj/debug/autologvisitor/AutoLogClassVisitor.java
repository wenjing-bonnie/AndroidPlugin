package com.android.wj.debug.autologvisitor;

import com.android.wj.debug.utils.SystemOutPrintln;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by wenjing.liu on 2021/8/3 in J1.
 * <p>
 * 访问所有.class的类成员
 *
 * @author wenjing.liu
 */
public class AutoLogClassVisitor extends ClassVisitor {

//    public AutoLogClassVisitor(int api) {
//        this(api,null);
//    }

    public AutoLogClassVisitor(ClassVisitor visitor){
        super(Opcodes.ASM4,visitor);
    }

//    public AutoLogClassVisitor(int api, ClassVisitor visitor) {
//        super(api, visitor);
//    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        SystemOutPrintln.println("version = " + version);
    }
}

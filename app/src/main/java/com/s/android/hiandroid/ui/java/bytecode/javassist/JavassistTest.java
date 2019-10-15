package com.s.android.hiandroid.ui.java.bytecode.javassist;

import com.s.android.hiandroid.ui.java.bytecode.Base;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class JavassistTest {

    public static void main(String[] args) throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("meituan.bytecode.javassist.Base");
        CtMethod m = cc.getDeclaredMethod("process");
        m.insertBefore("{ System.out.println(\"start\"); }");
        m.insertAfter("{ System.out.println(\"end\"); }");
        Class c = cc.toClass();
        cc.writeFile("/Users/zen/projects");
        Base h = (Base) c.newInstance();
        h.process();
    }
}

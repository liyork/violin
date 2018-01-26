package com.wolf.test.javassist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

/**
 * Description:
 * <br/> Created on 2018/1/24 14:44
 *
 * @author 李超
 * @since 1.0.0
 */
public class TestReplace {

    @org.junit.Test
    public void test03() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("com.wolf.test.agent.targetobj.TimeTest");

        CtMethod[] declaredMethods = cc.getDeclaredMethods();
        for (CtMethod cm : declaredMethods) {
            //System.out.println(cm.getName());
            //拦截方法内部调用其他方法
            cm.instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    //System.out.println(m.getClassName() + " " + m.getMethodName());
                    if ("com.wolf.test.agent.targetobj.TimeTest".equals(m.getClassName()) && "test".equals(m.getMethodName())) {
                        m.replace("{long startMs = System.currentTimeMillis(); " +
                                "$_ = $proceed($$); " +
                                "long endMs = System.currentTimeMillis();" +
                                "System.out.println(\"Executed in ms: \" + (endMs-startMs));}");
                    }
                }
            });
        }


        byte[] bytes = cc.toBytecode();

        File file = new File("D:\\TimeTest2.class");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes);
        fileOutputStream.close();
//        MethodInfo methodInfo2 = cm.getMethodInfo();
//        CodeAttribute codeAttribute = methodInfo2.getCodeAttribute();
//        byte[] bytes = codeAttribute.getCode();
//        System.out.println(new String(bytes));

        Class clazz = cc.toClass();
        Object obj = clazz.newInstance();  //������������Emp������������������������������Emp������
        Method method = clazz.getDeclaredMethod("sayHello3", int.class);
        method.invoke(obj, 300);
    }
}

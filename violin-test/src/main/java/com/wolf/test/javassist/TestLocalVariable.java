package com.wolf.test.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

/**
 * Description:
 * ClassPool.doPruning=true  CtClass.detach() 删除类，释放内存
 * <p>
 * //解决加载问题，可以指定一个未加载的ClassLoader
 * Class c3 = cc.toClass(new MyClassLoader());
 * <p>
 * Loader cl = new Loader(pool);
 * <p>
 * HotSwapper
 * <p>
 * addCatch m.addCatch("{ System.out.println($e); throw $e; }", etype);
 * <p>
 * $0代表的是this，$1代表方法参数的第一个参数  $args  $args[0]对应的是$1,而不是$0，$0!=$args[0]，$0=this
 * <p>
 * $$是所有方法参数的简写 move($$)
 * <p>
 * $cflow意思为控制流 if ($cflow(fact) == 0)
 * <p>
 * $r 指的是方法返回值的类型 如返回值为Integer，则$r为int。如果返回值为void，则该值为null。
 * <p>
 * $_代表的是方法的返回值
 * <p>
 * pool.importPackage("java.awt");
 * <p>
 * <p>
 * <br/> Created on 2018/1/24 14:44
 *
 * @author 李超
 * @since 1.0.0
 */
public class TestLocalVariable {

    @org.junit.Test
    public void test03() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("com.wolf.test.agent.targetobj.TimeTest");

        CtMethod cm = cc.getDeclaredMethod("sayHello3", new CtClass[]{CtClass.intType});
        cm.insertBefore("int a = 1 ;\n System.out.println($1);System.out.println(\"start!!!\");");
        cm.insertBefore("\n System.out.println(\"test\");");//before永远插在前面
        cm.addLocalVariable("b", CtClass.intType);
        cm.insertAt(9, " b=3;System.out.println(\"b=\"+b);");


//        cm.insertAt(50, "System.out.println(\"b=123123\"+b);");
        cm.insertAfter("System.out.println(\"end!!!,b=\"+b);");

        byte[] bytes = cc.toBytecode();

        File file = new File("D:\\TimeTest.class");
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

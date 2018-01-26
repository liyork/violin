package com.wolf.test.javassist;

import javassist.*;

/**
 * Description:
 * <br/> Created on 2018/1/24 18:27
 *
 * @author 李超
 * @since 1.0.0
 */
public class TestMake {

    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass("com.bjsxt.bean.Emp");

        //创建属性
        CtField f1 = CtField.make("private int empno;", cc);
        CtField f2 = CtField.make("private String ename;", cc);
        cc.addField(f1);
        cc.addField(f2);

        //创建方法
        CtMethod m1 = CtMethod.make("public int getEmpno(){return empno;}", cc);
        CtMethod m2 = CtMethod.make("public void setEmpno(int empno){this.empno=empno;}", cc);
        CtMethod m3 = CtMethod.make("public void sayHello(int empno){}", cc);
        cc.addMethod(m1);
        cc.addMethod(m2);
        cc.addMethod(m3);

        CtConstructor constructor1 = new CtConstructor(new CtClass[]{}, cc);
        constructor1.setBody("{}");
        cc.addConstructor(constructor1);
        //添加构造器
        CtConstructor constructor = new CtConstructor(new CtClass[]{CtClass.intType, pool.get("java.lang.String")}, cc);
        constructor.setBody("{this.empno=empno; this.ename=ename;}");
        cc.addConstructor(constructor);


        cc.writeFile("D:\\myjava"); //将上面构造好的类写入到c:/myjava中
        System.out.println("生成类，成功！");
    }
}

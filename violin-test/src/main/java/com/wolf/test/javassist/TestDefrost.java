package com.wolf.test.javassist;

import javassist.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Description:
 * <br/> Created on 2018/1/24 19:38
 *
 * @author 李超
 * @since 1.0.0
 */
public class TestDefrost {


    public static void main(String[] args) throws Exception {

        ClassPool pool = ClassPool.getDefault();

        CtClass ctClass = pool.makeClass("com.study.javassist.MyCC");

        // 添加一个参数
        CtField ctField = new CtField(CtClass.intType, "id", ctClass);
        ctField.setModifiers(Modifier.PUBLIC);
        ctClass.addField(ctField);

        ctClass.writeFile("D:\\myclass");
        pool.appendClassPath("D:\\myclass");

        //write2ClassFile(ctClass);
        byte[] byteArr;
        FileOutputStream fos;

        //当CtClass对象通过writeFile()、toClass()、toBytecode()转化为Class后，Javassist冻结了CtClass对象
        // 解冻CtClass对象
        ctClass.defrost();
        // 为了测试ctClass是否能够再修改，再添加一个域
        CtField ctField2 = new CtField(pool.get("java.lang.String"), "name", ctClass);
        ctField2.setModifiers(Modifier.PUBLIC);
        ctClass.addField(ctField2);
        //write2ClassFile(ctClass);

        //重命名已冻结的类
        CtClass ct2 = pool.getAndRename("com.study.javassist.MyCC", "com.study.javassist.TestName2");
        System.out.println(ct2.getName());
    }

    // 把生成的class文件写入文件，这个不带包名
    private static void write2ClassFile(CtClass ctClass) throws IOException, CannotCompileException {
        byte[] byteArr = ctClass.toBytecode();
        FileOutputStream fos = new FileOutputStream(new File("D://MyCC.class"));
        fos.write(byteArr);
        fos.close();
        System.out.println("over!!");
    }
}

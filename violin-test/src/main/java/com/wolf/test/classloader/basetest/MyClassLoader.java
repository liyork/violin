package com.wolf.test.classloader.basetest;


import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 * 方法区中的class结构来自于class文件，类被加载后生成Class对象(作为程序代码访问类型数据的外部接口)，然后new时使用Class对象进行构造
 * <p>
 * 使用agent重新构造的类在方法区是否一个？
 * <br/> Created on 2018/6/20 12:26
 *
 * @author 李超
 * @since 1.0.0
 */
public class MyClassLoader extends ClassLoader {
    //注意：要考虑有充分理由才能破坏双亲委派机制，否则最好重写findclass
    //自己加载类
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        String fileName = name.substring(name.lastIndexOf("") + 1) + ".class";
        InputStream is = getClass().getResourceAsStream(fileName);
        if (is == null) {
            return super.loadClass(name);
        }
        byte[] b = new byte[0];
        try {
            b = new byte[is.available()];
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(name, b, 0, b.length);
    }

    public static void main(String[] args) {
        MyClassLoader myClassLoader = new MyClassLoader();
        try {
            Object customizeObject = myClassLoader.loadClass("com.wolf.test.classloader.basetest.MyClassLoader").newInstance();
            System.out.println("MyClassLoader instance:" + customizeObject);
            //false，customizeObject由自定义类加载器加载，MyClassLoader由app加载器加载
            System.out.println("customizeObject instanceof MyClassLoader:" + (customizeObject instanceof MyClassLoader));
            System.out.println("objClass.getClassLoader()" + customizeObject.getClass().getClassLoader());
            System.out.println("MyClassLoaderClass ClassLoader:" + MyClassLoader.class.getClassLoader());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

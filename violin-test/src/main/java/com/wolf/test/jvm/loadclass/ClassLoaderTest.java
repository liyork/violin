package com.wolf.test.jvm.loadclass;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 * <br/> Created on 11/2/17 10:03 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class ClassLoaderTest {

    static class MyClassLoader extends ClassLoader {
        //注意：要考虑有充分理由才能破坏双亲委派机制，否则最好重写findclass
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
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
            return defineClass(name,b,0,b.length);
        }
    }

    public static void main(String[] args) {
        MyClassLoader myClassLoader = new MyClassLoader();
        try {
            Object obj = myClassLoader.loadClass("com.wolf.test.jvm.loadclass.ClassLoaderTest").newInstance();
            System.out.println(obj);
            System.out.println(obj instanceof ClassLoaderTest);//false
            System.out.println(obj.getClass().getClassLoader());
            System.out.println(ClassLoaderTest.class.getClassLoader());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

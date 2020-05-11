package com.wolf.test.classloader.basetest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <p> Description: 自定义类加载器
 * 加载器父子关系：bootstrap---extension(ext)---system(app)---classloader(自定义)
 * 类继承关系：classloader -- secureClassLoader -- urlClassLoader -- launcher$ext launcher$app
 * Java 虚拟机不仅要看类的全名是否相同, 还要看加载此类的类加载器是否一样
 * 用途：
 * 可以从多个地方加载类，比如网络上，数据库中，甚至即时的编译源文件获得类文件；
 * 类加载器可以在运行时原则性的加载某个版本的类文件；
 * 类加载器可以动态卸载一些类；
 * 类加载器可以对类进行解密解压缩后再载入类
 * <p>
 * 真正完成类的加载工作是通过调用 defineClass来实现的；
 * 而启动类的加载过程是通过调用 loadClass来实现的。
 * 前者称为一个类的定义加载器（defining loader），
 * 后者称为初始加载器（initiating loader）。
 * 在 Java 虚拟机判断两个类是否相同的时候，使用的是类的定义加载器
 * 方法 loadClass()抛出的是 java.lang.ClassNotFoundException异常；
 * 方法 defineClass()抛出的是 java.lang.NoClassDefFoundError异常
 * <p>
 * 线程上下文classloader
 * Service Provider Interface，SPI
 * <p>
 * Class.forName默认初始化类
 * loadClass不初始化类
 * <p>
 * NetworkClassLoader加载了某个版本的类之后，第一种做法是使用 Java 反射 API。另外一种做法是使用接口
 * 并不能直接在客户端代码中引用从服务器上下载的类，因为客户端代码的类加载器找不到这些类
 * <p>
 * 自定义类加载器可以动态加载指定位置的类(网络传过来的，磁盘上的)
 * <p/>
 * Date: 2015/7/23
 * Time: 17:11
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class CustomizeClassLoader extends ClassLoader {

    private String baseDir;

    //如果没有通过构造函数指定parent是null,那么系统(app)加载器为父类,加载类走委派模式,如果为null则父类是bootstrap
    //有无父类加载器影响loadClass查找顺序,有：子类可以找到父类加载的类。
    public CustomizeClassLoader(String baseDir,ClassLoader parent) {
        //演示设定ext类加载器为父类，即向上寻找的就是ext了，app里面的不找了。
//        super(ClassLoader.getSystemClassLoader().getParent());
        //不指定父类
//		super(null);

        super(parent);

        this.baseDir = baseDir;
    }



    //自定义classloader需要重写findClass方法
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        //接口由系统类加载器加载
//        String substring = name.substring(name.lastIndexOf("") + 1);
//        if (substring.equals("NonClassPathClassInterface") || name.startsWith("java") || substring.equals("Key")) {
//            return ClassLoader.getSystemClassLoader().loadClass(name);
//        }

        byte[] data = readFile(baseDir,name);

        if (null != data) {
            try {
                return defineClass(name, data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //上面没找到交给父加载器
        return super.findClass(name);
    }

    public static byte[] readFile(String baseDir,String name) {
        byte[] data = null;

        String substring = name.replace(".", "/");

        FileInputStream is = null;
        ByteArrayOutputStream bos = null;
        File file = new File(baseDir,substring + ".class");
        try {
            is = new FileInputStream(file);

            bos = new ByteArrayOutputStream();
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    bos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            data = bos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }


}

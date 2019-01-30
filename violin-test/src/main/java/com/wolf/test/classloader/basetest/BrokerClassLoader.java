package com.wolf.test.classloader.basetest;


/**
 * Description: 演示自定义加载顺序，
 * 注意：要考虑有充分理由才能破坏双亲委派机制，否则最好重写findclass
 *
 * 方法区中的class结构来自于class二进制文件，类被加载后生成Class对象(作为程序代码访问类型数据的外部接口)，然后new时使用Class对象进行构造
 * <p>
 * 使用agent重新构造的类在方法区是否一个？
 * <br/> Created on 2018/6/20 12:26
 *
 * @author 李超
 * @since 1.0.0
 */
public class BrokerClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        synchronized (getClassLoadingLock(name)) {

//            Class<?> loadedClass = findLoadedClass(name);
            Class<?> loadedClass = null;//为了测试暂时注释掉

            if (null == loadedClass) {
                if (name.startsWith("java.") || name.startsWith("javax")) {
                    try {
                        loadedClass = getSystemClassLoader().loadClass(name);
                    } catch (ClassNotFoundException e) {
                        //ignore
                    }
                } else {
                    try {
                        loadedClass = this.findClass(name);
                    } catch (ClassNotFoundException e) {
                        //ignore
                    }

                    if (null == loadedClass) {
                        if (null != getParent()) {
                            loadedClass = getParent().loadClass(name);
                        } else {
                            loadedClass = getSystemClassLoader().loadClass(name);
                        }
                    }
                }
            }

            if (null == loadedClass) {
                throw new ClassNotFoundException("the class " + name + " not found.");
            }

            if (resolve) {
                resolveClass(loadedClass);
            }

            return loadedClass;
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        byte[] data = CustomizeClassLoader.readFile("/Users/lichao30/intellij_workspace/concurrenttest/target/classes", name);

        if (null != data) {
            try {
                return defineClass(name, data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void main(String[] args) {
        BrokerClassLoader brokerClassLoader = new BrokerClassLoader();
        try {
            String classFullName = "com.wolf.test.classloader.basetest.BrokerClassLoader";
            Object customizeObject = brokerClassLoader.loadClass(classFullName).newInstance();
            System.out.println("customizeObject instance:" + customizeObject);
            //false，customizeObject由自定义类加载器加载，BrokerClassLoader由app加载器加载
            System.out.println("customizeObject instanceof BrokerClassLoader:" + (customizeObject instanceof BrokerClassLoader));
            System.out.println("objClass.getClassLoader()" + customizeObject.getClass().getClassLoader());
            System.out.println("BrokerClassLoader.ClassLoader:" + BrokerClassLoader.class.getClassLoader());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

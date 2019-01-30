package com.wolf.test.classloader.basetest;

import javax.xml.stream.events.Namespace;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/01/29
 */
public class NameSpaceTest {

    public static void main(String[] args) throws ClassNotFoundException {

        ClassLoader classLoader = Namespace.class.getClassLoader();
        Class<?> aClass = classLoader.loadClass("com.wolf.test.classloader.basetest.NameSpaceTest");
//        Class<?> aClass = classLoader.loadClass("com.wolf.test.classloader.basetest.NameSpaceTest");

    }
}

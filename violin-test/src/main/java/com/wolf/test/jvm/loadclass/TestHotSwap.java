package com.wolf.test.jvm.loadclass;

import java.lang.reflect.Method;

/**
 * Description:
 * <br/> Created on 2018/1/22 9:45
 *
 * @author 李超
 * @since 1.0.0
 */
public class TestHotSwap {
    public static void main(String[] args) throws Exception {
        testHotswap();

//        testLoadFromJar();
//
//        // 执行一次gc垃圾回收
//        System.gc();
//        System.out.println("GC over Unloading class");
//        Thread.sleep(5000);
    }

    private static void testLoadFromJar() throws ClassNotFoundException {
        HotSwapURLClassLoader classLoader = new HotSwapURLClassLoader("xx", "D:\\intellijWrkSpace\\violin\\violin-test\\target\\classes\\hot.jar");
        // 如果Hot类被修改了，那么会重新加载，hotClass也会返回新的
        Class hotClazz = classLoader.loadClass("com.wolf.test.jvm.loadclass.Hot2");
        System.out.println(hotClazz);
    }

    private static void testHotswap() {
        //开启线程，如果class文件有修改，就热替换
        Thread t = new Thread(new MonitorHotSwap());
        t.start();
    }

    public static class MonitorHotSwap implements Runnable {
        // Hot就是用于修改，用来测试热加载
        private String className = "com.wolf.test.jvm.loadclass.Hot";
        private Class hotClazz = null;
        private HotSwapURLClassLoader hotSwapCL = null;

        @Override
        public void run() {
            try {
                while (true) {
                    loadClass();
                    Object hot = hotClazz.newInstance();
                    Method m = hotClazz.getMethod("hot");
                    m.invoke(hot, null); //打印出相关信息
                    // 每隔10秒重新加载一次
                    Thread.sleep(10000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 加载class
         */
        public void loadClass() throws Exception {
            hotSwapCL = new HotSwapURLClassLoader("D:\\intellijWrkSpace\\violin\\violin-test\\target\\classes");
            // 如果Hot类被修改了，那么会重新加载，hotClass也会返回新的
            hotClazz = hotSwapCL.loadClass(className);
        }
    }
}
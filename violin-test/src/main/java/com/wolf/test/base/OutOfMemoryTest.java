package com.wolf.test.base;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * 现在java7够强的，还能分析出来这个map还能给回收了。。。。
 * 最后不行了。报了个Exception in thread "pool-1-thread-1" java.lang.OutOfMemoryError: GC overhead limit exceeded
 * <br/> Created on 2017/6/30 16:43
 *
 * @author 李超
 * @since 1.0.0
 */
public class OutOfMemoryTest {

    private static Map<Integer, String> map = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
//        test1();  有gc，无法溢出
//        test2();  可以溢出
//        test3();  //可以溢出，但是分析后不对
//        test4();  //可以溢出
//        testHeapOOM();
        testPermGenOOM();
    }

    private static void read() {
        for(Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry + " " + entry.getKey() + " " + entry.getValue());
        }
    }

    public static void test1() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        read();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

        Random random = new Random(100000000);
        for(int i = 0; i < 100000000; i++) {
            map.put(i, random.nextInt(100000000) + "sdfqwerwer");
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void test2() {
        Vector v = new Vector();
        for(int i = 0; i < 250000; i++) {
            v.add(new byte[1 * 1024 * 1024]);
            System.out.println("i:" + i);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("1111111");
    }

    //由于编译器优化代码后将 long[] arr = new long[i];放入每次循环中。
    // 导致最后一次分配数组内存不足时先进行了gc，然后dump出的文件没有任何long[]相关信息
    public static void test3() {
        long arr[];
        for(int i = 1; i <= 10000000; i *= 2) {
            arr = new long[i];
            System.out.println("i:" + i);
        }
    }

    //编译器没有进行优化，最后一次分配数组内存不足时还保留上一个大数组，dump出来后就有long[]了。。
    public static void test4() {

        long[] arr = new long[0];
        for(int i = 1; i <= 10000000; i *= 2) {
            arr = new long[i];
        }
    }

    //-Xms2m -Xmx2m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/Users/chaoli/workspace
    public static void testHeapOOM() throws InterruptedException {
        List<OOMObject> oomObjects = new ArrayList<>();
        while (true) {
            oomObjects.add(new OOMObject());
           //Thread.sleep(1);
        }
    }

    //new Object[10000000]这个就撑爆了 ：OutOfMemoryError:Java heap space ,似乎jdk7就把方法区移进了heap？
    public static void testPermGenOOM() throws InterruptedException {
        Object[] array = new Object[1000000];
        for(int i=0; i<1000000; i++){
            String d = String.valueOf(i).intern();//new Object[1000000]：OutOfMemoryError: GC overhead limit exceeded ，
            array[i]=d;
        }
    }
}

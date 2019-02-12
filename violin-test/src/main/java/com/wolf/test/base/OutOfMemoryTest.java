package com.wolf.test.base;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    public static void main(String[] args) throws InterruptedException, IllegalAccessException {
//        test1();  有gc，无法溢出
//        test2();  //可以溢出
        test2Catch();  //可以溢出
//        test3();  //可以溢出，但是分析后不对
//        test4();  //可以溢出
//        testHeapOOM();
//        testPermGenOOM();

//        try {
//            testStackLeak();
//        } catch (Exception e) {
//            System.out.println("stack length:"+stackLength);
//            e.printStackTrace();
//        }

//        testMethodAreaOOM();

//        testDirectMemoryOOM();
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


//    Throwable
//            Exception
//                  RuntimeException
//            Error
//                  VirtualMachineError
//                      OutOfMemoryError
    public static void test2Catch() {
        Vector v = new Vector();
        try {
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
//        } catch (Exception e) {
        } catch (Throwable e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 20; i++) {
            System.out.println(i+"_xxxx");
        }
        //这里再分配也是溢出，这里能干什么？释放已有的某些内存？怎么获取？
        v.add(new byte[1 * 1024 * 1024]);
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
        List<MemoryObject> memoryObjects = new ArrayList<>();
        while (true) {
            memoryObjects.add(new MemoryObject());
           //Thread.sleep(1);
        }
    }

    //-XX:PermSize=10M -XX:MaxPermSize=10M
    //new Object[10000000]这个就撑爆了 ：OutOfMemoryError:Java heap space ,似乎jdk7就把方法区移进了heap？确实
    public static void testPermGenOOM() throws InterruptedException {
        Object[] array = new Object[1000000];
        for(int i=0; i<1000000; i++){
            String d = String.valueOf(i).intern();//new Object[1000000]：OutOfMemoryError: GC overhead limit exceeded ，
            array[i]=d;
        }
    }

    static int stackLength = 1;

    //-Xss128k 栈大小
    public static void testStackLeak(){
        stackLength++;
        testStackLeak();
    }

    //-XX:PermSize=10M  -XX:PermSize=10M
    //OutOfMemoryError: PermGen space,要注意jsp动态生成类，不同classloader加载同一个class也被视为不同类
    public static void testMethodAreaOOM(){
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(MemoryObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    return methodProxy.invokeSuper(o,objects);
                }
            });
            enhancer.create();
        }
    }

    //-Xmx10M -XX:MaxDirectMemorySize=5M
    //mac 上没有出现异常。不知道是不是和系统内核有关。应该是:OutOfMemoryError,
    //由DirectMemory导致的内存溢出，heapDump文件不会有明显异常，而且很小，可能程序中使用了nio
    public static void testDirectMemoryOOM() throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(1024 * 1024);
        }
    }
}

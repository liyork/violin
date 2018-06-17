package com.wolf.test.base;

import com.wolf.test.entity.Person;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * WeakReference与SoftReference都可以用来保存对象的实例引用，这两个类与垃圾回收有关。
 * <p>
 * SoftReference是强引用，它保存的对象实例，除非JVM即将OutOfMemory，否则不会被GC回收。
 * 这个特性使得它特别适合设计对象Cache。对于Cache，我们希望被缓存的对象最好始终常驻内存，
 * 但是如果JVM内存吃紧，为了不发生OutOfMemoryError导致系统崩溃，必要的时候也允许JVM回收Cache的内存，
 * 待后续合适的时机再把数据重新Load到Cache中。这样可以系统设计得更具弹性。
 * <p>
 * WeakReference是弱引用，其中保存的对象实例可以随时被GC回收掉。
 * 这个类通常用于在某处保存对象引用，而又不干扰该对象被GC回收，通常用于Debug、内存监视工具等程序中。
 * 因为这类程序一般要求即要观察到对象，又不能影响该对象正常的GC过程。
 * 最近在JDK的Proxy类的实现代码中也发现了Weakrefrence的应用，
 * Proxy会把动态生成的Class实例暂存于一个由Weakrefrence构成的Map中作为Cache。
 * <p>
 * 使用之前要判断是否为null
 * <p>
 * 如果只是想避免OutOfMemory异常的发生，则可以使用软引用。如果对于应用的性能更在意，想尽快回收一些占用内存比较大的对象，则可以使用弱引用。
 * 还有就是可以根据对象是否经常使用来判断。如果该对象可能会经常使用的，就尽量用软引用。如果该对象不被使用的可能性更大些，就可以用弱引用。
 * <p>
 * 防止内存泄漏：
 * 第一，是在声明对象引用之前，明确内存对象的有效作用域。
 * 第二，在内存对象不再需要时，记得手动将其引用置空。
 * <p>
 * Java 的 GC 机制是建立在跟踪内存的引用机制上的
 * <p>
 * <p>
 * <br/> Created on 2017/2/22 11:39
 * WeakReference
 *
 * @author 李超
 * @since 1.0.0
 */
public class ReferenceTest {

    //不断堆积内存
    private static Map<Integer, Person> map = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
//        testWeakReference();
//        testSoftReference();
        testQueue();
    }

    //jvm扫描到，不管内存是否紧张都清除
    private static void testWeakReference() {
        Person xx = new Person(1, "xx");
        WeakReference<Person> weakReference = new WeakReference<Person>(xx);
        xx = null;//清除强引用，否则不会被清除

        int i = 0;
        while (true) {
            if (weakReference.get() != null) {//不为空时，增加内存使用率，达到让jvm检测到弱引用并清除
                map.put(i++, new Person(i, "xx" + i++));
                System.out.println("new Person");
            } else {
                System.out.println("Object has been collected.");
                break;
            }
        }
    }

    //只有内存不足时才释放？除非oom才会释放。。
//      0.00   0.00 100.00 100.00  68.82  68.66     19    6.373    22   73.453   79.826
//      0.00   0.00 100.00 100.00  68.82  68.66     19    6.373    23   77.615   83.988
//      0.00   0.00 100.00 100.00  68.82  68.66     19    6.373    24   81.779   88.151
    //这都不释放。。
    private static void testSoftReference() {
        Person xx = new Person(1, "xx");
        SoftReference<Person> softReference = new SoftReference<Person>(xx);
        xx = null;

        int i = 0;
        while (true) {
            if (softReference.get() != null) {
                map.put(i++, new Person(i, "xx"));
                System.out.println("new Person");
            } else {
                System.out.println("Object has been collected.");
                break;
            }
        }
    }

    private static void testQueue() throws InterruptedException {

        ReferenceQueue queue = new ReferenceQueue();//当一个软引用、弱引用或虚引用的对象被GC回收
        new Thread(
                () -> {
                    while (true) {
                        try {
                            Reference reference = queue.remove();
                            System.out.println(reference + "回收了");
                            //清理工作。
                        } catch (InterruptedException e) {

                        }

                    }
                }).start();
        Object o = new Object();
        Reference root = new WeakReference(o, queue);
        System.out.println(root);
        o = null;
        System.gc();
        Thread.sleep(2000);
        Object o1 = root.get();
        System.out.println("o1="+o1);
    }


}

package com.wolf.test.base.reference;

import com.wolf.test.base.MemoryObject;
import com.wolf.test.entity.Person;

import java.lang.ref.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * 对象被new出来后的引用就是strongRef.
 * <p>
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

    //不断堆积的内存
    private static Map<Integer, Person> takeMemoryMap = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
//        testWeakReference();
//        testSoftReference();
//        testRefQueue();

//        lruCacheStrongRefTest();
//        lruCacheStrongRefGCTest();
//        lruCacheSoftRefGCTest();

        testPhantomRef();
    }

    //jvm扫描到，不管内存是否紧张都清除，不论是yound gc还是full gc
    private static void testWeakReference() {

        Person xx = new Person(1, "xx");
        WeakReference<Person> weakReference = new WeakReference<>(xx);
        xx = null;//清除强引用，否则不会被清除，若是不设置null，则jvm运行中一直有栈中强引用到Person对象，则不能被回收

        int i = 0;
        while (true) {
            if (weakReference.get() != null) {//不为空时，增加内存使用率，达到让jvm检测到弱引用并清除
                takeMemoryMap.put(i++, new Person(i, "xx" + i++));
                System.out.println("new Person");
            } else {//为空表示jvm对WeakReference关联的对象占用的堆内存进行了回收
                System.out.println("Person has been collected.");
                break;
            }
        }
    }

    //只有内存不足时才释放？除非oom才会释放。。
//    jstat -gcutil 11480 1000 100
//          S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT
//         79.05   0.00  59.61  44.55  70.73  71.25      6    0.650     2    1.904    2.554
//         79.05   0.00  92.94  44.55  70.73  71.25      6    0.650     2    1.904    2.554
//         0.00  99.98   1.99  64.66  70.73  71.25      7    0.905     2    1.904    2.810
//         0.00  99.98  15.93  64.66  70.73  71.25      7    0.905     2    1.904    2.810
    //这都不释放。。
    private static void testSoftReference() {
        Person xx = new Person(1, "xx");
        SoftReference<Person> softReference = new SoftReference<>(xx);
        xx = null;

        int i = 0;
        while (true) {
            if (softReference.get() != null) {
                takeMemoryMap.put(i++, new Person(i, "xx"));
                System.out.println("new Person");
            } else {
                System.out.println("Object has been collected.");
                break;
            }
        }
    }

    private static void testRefQueue() throws InterruptedException {

        ReferenceQueue<Object> queue = new ReferenceQueue<>();//当一个软引用、弱引用或虚引用的对象被GC回收时，会放入队列

        new Thread(() -> {
            while (true) {
                try {
                    Reference reference = queue.remove();//阻塞直到有对象被回收
                    System.out.println(reference + "回收了");
                    //清理工作。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Object strongRef = new Object();
        Reference<Object> weakReference = new WeakReference<>(strongRef, queue);
        System.out.println("weakReference:" + weakReference);
        strongRef = null;

        System.gc();//若注释则不会有回收对象入队列
        Thread.sleep(2000);

        Object existObject = weakReference.get();
        System.out.println("existObject=" + existObject);
    }

    private static void lruCacheStrongRefTest() {

        StrongLRUCache<String, MemoryObject> cache = new StrongLRUCache<>(5, k -> new MemoryObject());
        cache.get("a");
        cache.get("b");
        cache.get("c");
        cache.get("d");
        cache.get("e");

        //清除掉a,添加f
        cache.get("f");
        System.out.println(cache);
    }

    //-Xmx128M -Xms64M -XX:+PrintGCDetails
    //the 97 MemoryObject stored at cache.
    //[Full GC (Ergonomics) [PSYoungGen: 14626K->14338K(29184K)] [ParOldGen: 87467K->87466K(87552K)] 102093K->101805K(116736K), [Metaspace: 4365K->4365K(1056768K)], 0.0078869 secs] [Times: user=0.03 sys=0.00, real=0.01 secs]
    //[Full GC (Allocation Failure) [PSYoungGen: 14338K->14338K(29184K)] [ParOldGen: 87466K->87466K(87552K)] 101805K->101805K(116736K), [Metaspace: 4365K->4365K(1056768K)], 0.0041358 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
    //Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
    //	at com.wolf.test.base.MemoryObject.<init>(MemoryObject.java:13)
    //	at com.wolf.test.base.reference.ReferenceTest.lambda$lruCacheStrongRefGCTest$2(ReferenceTest.java:155)
    //	at com.wolf.test.base.reference.ReferenceTest$$Lambda$1/422392391.load(Unknown Source)
    //	at com.wolf.test.base.reference.StrongLRUCache.get(StrongLRUCache.java:55)
    //	at com.wolf.test.base.reference.ReferenceTest.lruCacheStrongRefGCTest(ReferenceTest.java:159)
    //	at com.wolf.test.base.reference.ReferenceTest.main(ReferenceTest.java:60)
    //Heap
    // PSYoungGen      total 29184K, used 14848K [0x00000007bd580000, 0x00000007c0000000, 0x00000007c0000000)
    //  eden space 14848K, 100% used [0x00000007bd580000,0x00000007be400000,0x00000007be400000)
    //  from space 14336K, 0% used [0x00000007bf200000,0x00000007bf200000,0x00000007c0000000)
    //  to   space 14336K, 0% used [0x00000007be400000,0x00000007be400000,0x00000007bf200000)
    // ParOldGen       total 87552K, used 87466K [0x00000007b8000000, 0x00000007bd580000, 0x00000007bd580000)
    //  object space 87552K, 99% used [0x00000007b8000000,0x00000007bd56ab88,0x00000007bd580000)
    // Metaspace       used 4390K, capacity 4862K, committed 4992K, reserved 1056768K
    //  class space    used 483K, capacity 563K, committed 640K, reserved 1048576K
    private static void lruCacheStrongRefGCTest() throws InterruptedException {

        StrongLRUCache<String, MemoryObject> cache = new StrongLRUCache<>(200, k -> new MemoryObject());

        for (int i = 0; i < Integer.MAX_VALUE; i++) {

            cache.get(i + "");
            TimeUnit.MILLISECONDS.sleep(1000);
            System.out.println("the " + i + " MemoryObject stored at cache.");
        }
    }

    //不会引起内存溢出，但是sleep间隔很短，可能gc还没来及就已经溢出了。
    private static void lruCacheSoftRefGCTest() throws InterruptedException {

        SoftLRUCache<String, MemoryObject> cache = new SoftLRUCache<>(200, k -> new MemoryObject());

        for (int i = 0; i < Integer.MAX_VALUE; i++) {

            cache.get(i + "");
            TimeUnit.MILLISECONDS.sleep(1000);
            System.out.println("the " + i + " soft MemoryObject stored at cache.");
        }
    }

    //phantom ref必须和ReferenceQueue配合
    //get方法始终返回null
    //回收时放入ReferenceQueue
    //使用Phantom Reference进行清理动作要比Object的finalize更灵活
    private static void testPhantomRef() throws InterruptedException {

        ReferenceQueue<MemoryObject> referenceQueue = new ReferenceQueue<>();
        PhantomReference<MemoryObject> phantomReference = new PhantomReference<>(new MemoryObject(), referenceQueue);

        System.out.println(phantomReference.get());//始终null
        System.gc();

        TimeUnit.SECONDS.sleep(2000000000);

        Reference<? extends MemoryObject> existRef = referenceQueue.remove();
        System.out.println("existRef:" + existRef);

        //上面示例jvm不能终止
        //"Finalizer" #3 daemon prio=8 os_prio=31 tid=0x00007fb7b7000800 nid=0x3203 in Object.wait() [0x0000700003f02000]
        //   java.lang.Thread.State: WAITING (on object monitor)
        //	at java.lang.Object.wait(Native Method)
        //	- waiting on <0x00000007b8110318> (a java.lang.ref.ReferenceQueue$Lock)
        //	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)
        //	- locked <0x00000007b8110318> (a java.lang.ref.ReferenceQueue$Lock)
        //	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)
        //	at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:216)
        //
        //"main" #1 prio=5 os_prio=31 tid=0x00007fb7b500d000 nid=0x1a03 in Object.wait() [0x00007000033e1000]
        //   java.lang.Thread.State: WAITING (on object monitor)
        //	at java.lang.Object.wait(Native Method)
        //	- waiting on <0x00000007b810f988> (a java.lang.ref.ReferenceQueue$Lock)
        //	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)
        //	- locked <0x00000007b810f988> (a java.lang.ref.ReferenceQueue$Lock)
        //	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)
        //	at com.wolf.test.base.reference.ReferenceTest.testPhantomRef(ReferenceTest.java:208)
        //	at com.wolf.test.base.reference.ReferenceTest.main(ReferenceTest.java:60)

        //是由于Finalizer线程本身就是一直执行，当有要回收对象时，会remove，然后还会再等待，而我们代码中remove所以就一直等待了。
        //而 todo 去掉对象的finalize方法，则可以正常退出jvm，是不是Finalizer线程看到有对象实现finalize则真正remove，所以代码中的
        //remove就阻塞了，而要是没有实现finalize，那么Finalizer线程不会remove？
    }
}

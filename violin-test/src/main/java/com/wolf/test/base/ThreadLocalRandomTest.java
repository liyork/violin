package com.wolf.test.base;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Description:本以为用一个静态成员变量即可的随机生成，却引发了问题...
 *
 * 基本上就是每个线程有各自的seed，然后各自生成随机序列，不会使用cas提升效率。当然random也是线程安全的，
 * 不过效率没有这个好。空间换时间么
 *
 * 通过将线程安全放入其初始化部分，而不是在使用阶段，这就能够得到性能提升
 * <br/> Created on 1/8/2019
 *
 * @author 李超
 * @since 1.0.0
 */
public class ThreadLocalRandomTest {

    private static ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    public static void main(String[] args) throws InterruptedException {

        System.out.println(threadLocalRandom.nextInt(100));
        System.out.println(threadLocalRandom.nextInt(100));
        System.out.println(threadLocalRandom.nextInt(100));

        //下面当然是用错了，但是结果都一样，感觉像是一个种子引起的。。应该是没有调用localInit的原因
        //经过反复试验，确实是这样
        //main的初始种子是被随机生成的-2505210960241993469然后基于这个不断生成 8895503859081205016  1849474604694851885
        //而线程若是没有调用localInit那么初始种子是0
        //然后再生成的序列就相同了，-7046029254386353131  4354685564936845354  -2691343689449507777  8709371129873690708  1663341875487337577
        //总结：这个和random一个效果，new Random()用的当前时间做种子，再生成的序列就不一样了。
        //而new Random(seed)用的参数seed，所以生成的序列是一样的
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            for (int i = 0; i < 10; i++) {
                int i1 = threadLocalRandom.nextInt(100);
                System.out.print(i1 + " ");
            }

        }).start();

        System.out.println();
        Thread.sleep(2000);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            for (int i = 0; i < 10; i++) {
                int i1 = threadLocalRandom.nextInt(100);
                System.out.print(i1 + " ");
            }

        }).start();

        System.out.println();
        Thread.sleep(2000);

        //这个似乎用的自己的种子，因为内部调用了localInit
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            for (int i = 0; i < 10; i++) {
                ThreadLocalRandom current = ThreadLocalRandom.current();
                int i1 = current.nextInt(100);
                System.out.print(i1 + " ");
            }

        }).start();

        System.out.println();
        Thread.sleep(2000);

        //官方推荐用法
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            for (int i = 0; i < 10; i++) {
                //调用current返回的是单例instance，并对当前线程操作localInit
                int i1 = ThreadLocalRandom.current().nextInt(100);
                System.out.print(i1 + " ");
            }
        }).start();

        System.out.println();
    }

}

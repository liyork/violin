package com.wolf.test.thread.threadlocal;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p> Description:threadLocal专属每个独立线程，不会相互干涉
 * 内部使用原理：
 * threadLocal只是用来操作每个线程的工具，每个线程内部都有个map(这个是threadLocal内部定义的数据)用来存放每个threadlocal对象的
 * threadLocal的get方法，先从当前thread获取其内部的map，然后操作这个map引用。
 * 查找map中的threadLocal使用开方查找而非链表查找
 *
 * 注：每个线程用完threadlocal需要清除掉，不然下个线程有可能是通过线程池获取的，还保留着上次的信息
 *
 * <p/>
 * Date: 2015/9/28
 * Time: 19:39
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ThreadLocalTest {

    public static void main(String[] args) throws InterruptedException {
//        testThreadLocalIsRight();
        testIfNeedResetWhenUpdateProperty();
    }

    //测试每个线程互不影响，另外测试混合threadlocal和count++导致的问题
    private static void testThreadLocalIsRight() throws InterruptedException {
        final Person person = new Person();
        ExecutorService executorService = Executors.newFixedThreadPool(500);
        final CountDownLatch countDownLatch = new CountDownLatch(500);
        for(int i = 0; i < 500; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    person.increase();
                    System.out.println(Thread.currentThread().getName() + ",age=" + person.getAge());

                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        System.out.println(person.getCount());

        //深刻教训，机器卡死重启，万事要留个后路。。。(停个几秒，或者啥的。。能中断的)
//		while (person.getAge() == 0) {
//			testThreadLocalIsRight(person);
//		}
    }


    /**
     * 对于取膜操作，
     * 想获得0-9，则用任意数%10，可能大神觉得这样费性能
     * 对于&操作，只有都为1的位才能1，所以尽可能的让位中含1则可以达到与取模作用相同但性能要好
     * 这就是为什么threadlocal中，The initial capacity -- MUST be a power of two.
     * 对于7----0111,任意数字&它，范围都会在0000-0111之间，即0-7
     * 2^N – 1 的二进制表示就是 N – 1 个 1
     */
    @Test
    public void testBitManipulation() {
        for(int i = 0; i < 50; i++) {
            System.out.println("i=" + i + " " + (i & (16 - 1)));
        }

        //实现跳跃性随机分布，随着threadlocal被回收，可能随机比顺序分配好
        int base = 0;
        for(int i = 0; i < 50; i++) {
            System.out.println(base & (16 - 1));
            base += 0x61c88647;
        }
    }


    /**
     * 采用“开放定址法”解决冲突的hashmap
     * 测试多个threadlocal对于一个线程，内部是如何构造的。使用 hash & (table-1)，不在则新建，如果有冲突则nextIndex。
     */
    @Test
    public void testMultiThreadLocal() {
        ContainMultiTL containMultiTL = new ContainMultiTL();
        containMultiTL.threadLocal1.get();
        containMultiTL.threadLocal2.get();
    }

    public static void testIfNeedResetWhenUpdateProperty() {
        final Person1 person1 = new Person1();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                person1.getAndSet();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                person1.getAndSet2();
            }
        });

        thread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.start();
    }

}
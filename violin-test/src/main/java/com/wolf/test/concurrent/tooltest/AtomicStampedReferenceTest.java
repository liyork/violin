package com.wolf.test.concurrent.tooltest;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Description:使用标记解决cas的aba问题
 * <br/> Created on 3/6/18 9:42 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class AtomicStampedReferenceTest {

    public static void main(String[] args) throws InterruptedException {
//        testABAProblem();

        //testBase();

        testResolveABA();

    }

    //ab线程都在执行cas的加，c线程在ab之间进行了减，本来没有c时，ab只能一个成功，但是若c在中间，那么b看到的是a+后c-的值。
    //下面场景就是满足10元为客户充值一次，但是由于客户消费了，那么很有可能充值多次。
    private static void testABAProblem() throws InterruptedException {

        AtomicInteger money = new AtomicInteger(10);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int monetyTmp = money.get();
                if (monetyTmp == 10) {
                    //免费充值
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean result = money.compareAndSet(monetyTmp, monetyTmp + 10);
                    System.out.println(Thread.currentThread().getName()+" produce compareAndSetResult:"+result+" after value:"+money.get());
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int monetyTmp = money.get();
                if (monetyTmp == 10) {
                    //免费充值
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean result = money.compareAndSet(monetyTmp, monetyTmp + 10);
                    System.out.println(Thread.currentThread().getName()+" produce compareAndSetResult:"+result+" after value:"+money.get());
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int monetyTmp = money.get();
                if (monetyTmp == 20) {
                    //消费
                    boolean result = money.compareAndSet(monetyTmp, monetyTmp - 10);
                    System.out.println(Thread.currentThread().getName()+" consumer compareAndSetResult:"+result+" after value:"+money.get());
                }
            }
        }).start();
    }

    private static void testBase() throws InterruptedException {
        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<Integer>(1,1);
        for (int i = 1; i < 5; i++) {

            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        System.out.println(Thread.currentThread().getName()+" expectedReference:"+finalI+" newReference:"+(finalI + 1) +" expectStamp:"+finalI+ " newStamp:"+(finalI+1));
                        boolean b = atomicStampedReference.compareAndSet(finalI, (finalI + 1) , finalI, finalI + 1);
                        System.out.println(Thread.currentThread().getName()+" compareAndSet:"+b);
                        if (b) {
                            break;
                        }

                    }
                }
            }).start();
        }

        //finalI1 + "" ,字符串+操作，若未在编译期变成常量，则运行时会构造新的char[0]，那么用==就不对了
//        int finalI1= 1;
//        boolean b = atomicStampedReference.compareAndSet(finalI1 + "", (finalI1 + 1) + "", finalI1, finalI1 + 1);
//        System.out.println(b);


        Thread.sleep(5000);
        System.out.println("finish:"+atomicStampedReference.getReference());

        Thread.sleep(10000);
    }

    //stamp永远是++的，所以不会回去，那么就不会有aba问题了
    private static void testResolveABA() throws InterruptedException {

        AtomicStampedReference<Integer> money = new AtomicStampedReference<Integer>(10,1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int stamp = money.getStamp();
                int moneyTmp = money.getReference();
                if (moneyTmp == 10) {
                    //免费充值
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean result = money.compareAndSet(moneyTmp, moneyTmp + 10,stamp,stamp+1);
                    System.out.println(Thread.currentThread().getName()+" produce compareAndSetResult:"+result+" after value:"+money.getReference());
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int stamp = money.getStamp();
                int moneyTmp = money.getReference();
                if (moneyTmp == 10) {
                    //免费充值
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean result = money.compareAndSet(moneyTmp, moneyTmp + 10,stamp,stamp+1);
                    System.out.println(Thread.currentThread().getName()+" produce compareAndSetResult:"+result+" after value:"+money.getReference());
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int moneyTmp = money.getReference();
                int stampTmp = money.getStamp();
                if (moneyTmp == 20) {
                    //消费
                    boolean result = money.compareAndSet(moneyTmp, moneyTmp - 10,stampTmp,stampTmp+1);
                    System.out.println(Thread.currentThread().getName()+" consumer compareAndSetResult:"+result+" after value:"+money.getReference());
                }
            }
        }).start();
    }



}

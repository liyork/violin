package com.wolf.test.concurrent.thread.productandconsumer.onetoone.usesynchronized.multi;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/12
 * Time: 14:33
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Clerk2 {

    private static final int MAX_PRODUCT = 1;

    private static final int MIN_PRODUCT = 0;

    private int product = 0;


    public synchronized void addProduct() {
        while (this.product >= MAX_PRODUCT) {
            try {
                System.out.println("产品已满,进入等候");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        this.product++;
        System.out.println("生产者生产第" + this.product + "个产品.");
        notify();
    }

    public synchronized void getProduct() {
        while(this.product <= MIN_PRODUCT) {
            try {
                System.out.println("缺货,进入等待");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        System.out.println("消费者取走了第" + this.product + "个产品.");
        this.product--;
        notify();
    }
}

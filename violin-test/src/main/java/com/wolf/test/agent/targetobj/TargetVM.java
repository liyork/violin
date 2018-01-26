package com.wolf.test.agent.targetobj;

/**
 * Description:
 * <br/> Created on 2018/1/24 10:00
 *
 * @author 李超
 * @since 1.0.0
 */
public class TargetVM {

    public static void main(String[] args) throws InterruptedException {
        new Thread(new WaitThread()).start();
    }

    static class WaitThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                    System.out.println("threadname:" + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }
}

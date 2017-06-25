package com.wolf.test.concurrent.thread.runnable;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/23
 * Time: 11:48
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class TestStopRunnable implements Runnable {

    //使用volatile保持多线程间的可见性
    private volatile boolean isNeedRun = true;

    public void setNeedRun(boolean isNeedRun) {
        this.isNeedRun = isNeedRun;
    }

    @Override
    public void run() {
        System.out.println("====>111");
        for(int x = 0; x < 99999999; x++) {
            if(isNeedRun) {
                x++;
                int y = x + 1;
                int z = y * 333;
                int c = z + 333 - 999 * 333 / 4444;
                int i = c * 45559 * 2232 - 22;
                System.out.println(i);
            }else{
                break;
            }
        }
        System.out.println("====>222");
    }
};

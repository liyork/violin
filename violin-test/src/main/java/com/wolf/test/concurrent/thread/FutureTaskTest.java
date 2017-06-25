package com.wolf.test.concurrent.thread;

import com.wolf.utils.BaseUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Description:
 * <br/> Created on 2017/6/25 10:27
 *
 * @author 李超
 * @since 1.0.0
 */
public class FutureTaskTest {

    public static void main(String[] args) {

        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                BaseUtils.simulateLongTimeOperation(8000000);
                int a = 1;
                if(a == 1) {
                    throw new RuntimeException("xxx");
                }
                return "1234";
            }
        });

        Thread thread = new Thread(futureTask);
        thread.start();

        System.out.println("futureTask.get...before");
        try {
            String s = futureTask.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("ExecutionException");
            e.printStackTrace();
        }
        System.out.println("futureTask.get...after");
    }
}

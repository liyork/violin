package com.wolf.test.concurrent.thread.productandconsumer.disruptor;

import com.lmax.disruptor.WorkHandler;
import com.wolf.test.concurrent.thread.productandconsumer.onetoone.useblockingqueue.Food;
import javafx.concurrent.Worker;

import java.util.concurrent.BlockingQueue;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/12
 * Time: 12:59
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Consumer implements WorkHandler<Food> {

    @Override
    public void onEvent(Food food) throws Exception {
        System.out.println(Thread.currentThread().getName()+" id:"+food.getId());
    }
}
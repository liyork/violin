package com.wolf.algorithm.queue;

import org.junit.Test;

/**
 * Description:
 * 队列，FIFO(First In First Out)，先进先出。队头、队尾、入队、出队。
 * 优先级队列。
 *
 * @author 李超
 * @date 2020/01/01
 */
public class MyQueueTest {

    @Test
    public void testBase() {
//        MyQueueLinked<Integer> myQueue = new MyQueueLinked<>();
//        MyQueueArray<Integer> myQueue = new MyQueueArray<>();
        MyQueueStack<Integer> myQueue = new MyQueueStack<>();
        myQueue.put(1);
        myQueue.put(3);
        myQueue.put(2);
        myQueue.put(4);
        System.out.println("isEmpty1:" + myQueue.isEmpty());
        System.out.println("size1:" + myQueue.size());
        Integer pop = myQueue.pop();
        System.out.println("pop1:" + pop);
        pop = myQueue.pop();
        System.out.println("pop2:" + pop);
        System.out.println("size2:" + myQueue.size());
        System.out.println("isEmpty2:" + myQueue.isEmpty());
    }
}

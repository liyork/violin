package com.wolf.algorithm.stack;

import com.wolf.algorithm.queue.MyQueueArray;

/**
 * Description: 2个队列实现栈
 * 队列是先进先出，对于1队列，负责出入，2队列负责过度，
 * 出时1队若有一个元素则出，否则1队列除最后元素全出并入到2队列，最后一个元素返回，2队列再出并入到1队列。相当于每次取最后一个
 *
 * @author 李超
 * @date 2020/01/01
 */
public class MyStackQueue<E extends Comparable> {

    private MyQueueArray<E> myQueue1 = new MyQueueArray<>(20);
    private MyQueueArray<E> myQueue2 = new MyQueueArray<>(20);
    private int size;

    public boolean isEmpty() {
        return myQueue1.isEmpty() && myQueue2.isEmpty();
    }

    public void push(E e) {
        size++;

        myQueue1.put(e);
    }

    // 每次总是这么折腾也很费数组，可能需要循环使用
    public E peek() {
        if (isEmpty()) {
            return null;
        }

        if (myQueue1.size() == 1) {
            return myQueue1.peek();
        }

        int count = 0;// 弹出元素
        E result = null;
        while (!myQueue1.isEmpty()) {
            myQueue2.put(myQueue1.pop());
            count++;
            if (count == size - 1) {
                result = myQueue1.peek();
            }
        }

        while (!myQueue2.isEmpty()) {
            myQueue1.put(myQueue2.pop());
        }

        return result;
    }

    public E pop() {
        if (isEmpty()) {
            return null;
        }

        if (myQueue1.size() == 1) {
            return myQueue1.pop();
        }

        int count = 0;
        E result = null;
        while (!myQueue1.isEmpty()) {
            myQueue2.put(myQueue1.pop());
            count++;
            if (count == size - 1) {
                result = myQueue1.pop();
                break;
            }
        }

        while (!myQueue2.isEmpty()) {
            myQueue1.put(myQueue2.pop());
        }

        size--;

        return result;
    }

    public int size() {
        return size;
    }
}

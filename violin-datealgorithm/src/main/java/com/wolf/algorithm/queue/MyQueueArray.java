package com.wolf.algorithm.queue;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/12/31
 */
public class MyQueueArray<E> {

    private E[] queue;
    private int length = 10;
    private int headIndex = 0;
    private int tailIndex = 0;

    public MyQueueArray() {
        queue = (E[]) new Object[length];
    }

    public MyQueueArray(int length) {
        this.length = length;
        queue = (E[]) new Object[length];
    }

    public boolean isEmpty() {
        return headIndex == tailIndex;
    }

    public void put(E e) {
        queue[tailIndex++] = e;
    }

    public E pop() {
        if (isEmpty()) {
            return null;
        }

        return queue[headIndex++];
    }

    // 为MyStackQueue使用
    public E peek() {
        if (isEmpty()) {
            return null;
        }

        return queue[headIndex];
    }

    public int size() {
        if (isEmpty()) {
            return 0;
        }

        return tailIndex - headIndex;
    }
}

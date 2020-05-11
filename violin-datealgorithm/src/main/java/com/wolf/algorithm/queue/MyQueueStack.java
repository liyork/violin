package com.wolf.algorithm.queue;

import com.wolf.algorithm.stack.MyStackArray;

/**
 * Description: 2个栈实现队列
 * 栈是后进先出，对于1栈，负责入，2栈负责出，出时若本身有值则pop，否则从1栈一起入过来。
 *
 * @author 李超
 * @date 2020/01/01
 */
public class MyQueueStack<E extends Comparable> {

    private MyStackArray<E> myStack1 = new MyStackArray<>();
    private MyStackArray<E> myStack2 = new MyStackArray<>();
    private int size;

    public boolean isEmpty() {
        return myStack1.isEmpty() && myStack2.isEmpty();
    }

    public void put(E e) {
        size++;

        myStack1.push(e);
    }

    public E pop() {
        if (isEmpty()) {
            return null;
        }

        size--;

        if (myStack2.isEmpty()) {
            while (!myStack1.isEmpty()) {
                myStack2.push(myStack1.pop());
            }
        }

        return myStack2.pop();
    }

    public int size() {
        return size;
    }
}

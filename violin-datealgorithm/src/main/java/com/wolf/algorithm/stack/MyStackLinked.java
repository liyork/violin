package com.wolf.algorithm.stack;

import com.wolf.algorithm.Node;

/**
 * Description: 链表实现栈
 *
 * @author 李超
 * @date 2019/12/27
 */
public class MyStackLinked<E extends Comparable<E>> {

    private Node<E> top;
    private int size;

    // 栈中最小元素
    private E min;

    public void push(E e) {
        if (null == e) {
            return;
        }

        size++;

        Node<E> eNode = new Node<E>(e);
        eNode.setNext(top);
        top = eNode;

        if (null == min || e.compareTo(min) < 0) {
            min = e;
        }
    }

    public boolean isEmpty() {
        return top == null;
    }

    public E peek() {
        if (top == null) {
            return null;
        }
        return top.getItem();
    }

    public E pop() {
        if (top == null) {
            return null;
        }

        size--;

        E result = top.getItem();

        Node<E> next = top.getNext();
        top.setNext(null);
        top = next;

        return result;
    }

    public int size() {
        return size;
    }

    // 用O(1)时间复杂度求栈中最小元素，只能用空间换时间。
    public E getMin() {
        return min;
    }
}

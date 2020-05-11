package com.wolf.algorithm.queue;

import com.wolf.algorithm.Node;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/12/31
 */
public class MyQueueLinked<E extends Comparable> {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public boolean isEmpty() {
        return head == tail;
    }

    public void put(E e) {
        size++;
        Node<E> eNode = new Node<>(e);
        if (tail == null) {
            head = eNode;
            tail = eNode;
            return;
        }

        tail.setNext(eNode);
        tail = eNode;
    }

    public E pop() {
        if (head == null) {
            return null;
        }

        size--;

        E result = head.getItem();
        head = head.getNext();
        return result;
    }

    public int size() {
        return size;
    }
}

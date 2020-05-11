package com.wolf.algorithm;

import java.util.Objects;

/**
 * Description: 节点
 *
 * @author 李超
 * @date 2019/12/18
 */
public class Node<E extends Comparable> {

    private E item;
    private Node<E> next;

    public Node(E item) {
        this.item = item;
    }

    public E getItem() {
        return item;
    }

    public void setItem(E item) {
        this.item = item;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return this.item.equals(node.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, next);
    }

    @Override
    public String toString() {
        return item.toString();
    }
}

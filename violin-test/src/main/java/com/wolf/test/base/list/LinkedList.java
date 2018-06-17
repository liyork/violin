package com.wolf.test.base.list;

import java.util.Objects;

/**
 * Description:
 * <br/> Created on 2018/5/30 15:28
 *
 * @author 李超
 * @since 1.0.0
 */
public class LinkedList {

    private Node head;
    private Node tail;

    private void add(Node node) {
        if (null == head) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
    }

    private void printList() {
        Node curNode = head;
        while (curNode != null) {
            System.out.println(curNode);
            curNode = curNode.next;
        }
    }

    private void reverse() {
        Node preNode = null;
        Node curNode = head;
        Node nextNode;
        while (true) {
            nextNode = curNode.next;//记录
            curNode.next = preNode;//翻转
            preNode = curNode;//记录
            curNode = nextNode;//移动

            if (null == nextNode) {//最后一个元素
                head = preNode;
                return;
            }
        }
    }

    private boolean hasCycle() {
        Node first = head;
        Node second = head;

        while (second != null) {
            if (first.equals(second)) {
                return true;
            }
            first = first.next;//走一步
            second = second.next.next;//走两步
        }

        return false;
    }


    public static void main(String[] args) {
//        LinkedList linkedList = new LinkedList();
//        linkedList.add(new Node(1));
//        linkedList.add(new Node(2));
//        linkedList.add(new Node(3));

//        linkedList.printList();

//        linkedList.reverse();
//        linkedList.printList();


        LinkedList linkedList = new LinkedList();
        Node node = new Node(1);
        linkedList.add(node);
        linkedList.add(new Node(2));
        linkedList.add(new Node(3));
        linkedList.add(node);
        linkedList.hasCycle();
    }

    private static class Node {
        Integer item;
        Node next;

        public Node(Integer item) {
            this.item = item;
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
}

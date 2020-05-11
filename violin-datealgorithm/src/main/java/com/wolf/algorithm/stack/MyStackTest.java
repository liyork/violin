package com.wolf.algorithm.stack;

import org.junit.Test;

/**
 * Description: 栈
 * 是线性数据结构，在一个特定范围的存储单元中存储的数据。与线性表比，他们的插入和删除操作受到更多约束和限定，又称为限定性的线性表结构。
 * 栈，LIF(Last In First Out)，后进先出。栈顶、栈底、入栈、出栈。
 *
 * @author 李超
 * @date 2019/12/31
 */
public class MyStackTest {

    @Test
    public void testMyStack() {
//        MyStackArray<Integer> myStack = new MyStackArray<>();
//        MyStackLinked<Integer> myStack = new MyStackLinked<>();
        MyStackQueue<Integer> myStack = new MyStackQueue<>();

        myStack.push(1);
        myStack.push(3);
        myStack.push(2);
        myStack.push(4);

        System.out.println("isEmpty:" + myStack.isEmpty());
        Integer peek = myStack.peek();
        System.out.println("peek1:" + peek);
        Integer pop = myStack.pop();
        System.out.println("pop:" + pop);
        peek = myStack.peek();
        System.out.println("peek2:" + peek);
        System.out.println("isEmpty:" + myStack.isEmpty());
    }

    @Test
    public void testPush() {
//        MyStackArray<Integer> myStack = new MyStackArray<>();
        MyStackLinked<Integer> myStack = new MyStackLinked<>();
        for (int i = 0; i < 15; i++) {
            myStack.push(i);
        }

        int length = myStack.size();
        System.out.println(length);
    }

    @Test
    public void testGetMin() {
        MyStackLinked<Integer> myStack = new MyStackLinked<>();
        for (int i = 0; i < 15; i++) {
            myStack.push(i);
        }

        int min = myStack.getMin();
        System.out.println(min);
    }
}

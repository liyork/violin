package com.wolf.algorithm.stack;

import java.util.Arrays;

/**
 * Description: 数组实现栈
 *
 * @author 李超
 * @date 2019/12/27
 */
public class MyStackArray<E> {

    private E[] stack;
    private static int INIT_INDEX = -1;
    private int topIndex = INIT_INDEX;// 所在位置为当前栈顶元素下标
    private int length = 10;

    public MyStackArray() {
        stack = (E[]) new Object[length];
    }

    public void push(E e) {
        ensureCapacity();
        stack[++topIndex] = e;
    }

    private void ensureCapacity() {
        if (topIndex == length - 1) {
            length = length * 2;
        }
        stack = Arrays.copyOf(stack, length);
    }

    public boolean isEmpty() {
        return topIndex == INIT_INDEX;
    }

    public E peek() {
        if (topIndex == INIT_INDEX) {
            return null;
        }

        return stack[topIndex];
    }

    public E pop() {
        if (topIndex == INIT_INDEX) {
            return null;
        }

        E result = stack[topIndex];
        stack[topIndex--] = null;
        return result;
    }

    public int size() {
        return topIndex + 1;
    }
}

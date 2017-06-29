package com.wolf.test.concurrent.performance;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Description:可以保证数组中的内容对所有线程可见，而非仅仅是数组的引用
 * <br/> Created on 2017/6/29 15:50
 *
 * @author 李超
 * @since 1.0.0
 */
public class AtomicArray {

    public static void main(String[] args) {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);

    }
}

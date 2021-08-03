package com.wolf.test.base;

import java.util.Arrays;

/**
 * Description: arraycopy测试
 * Created on 2021/8/3 5:57 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class ArrayCopyTest {
    public static void main(String[] args) {
        byte[] originalBytes = {1, 2, 3, 4, 5, 6};
        byte[] targetBytes = {7, 8, 9};
        System.arraycopy(originalBytes, 2, targetBytes, 0, 3);
        System.out.println(Arrays.toString(targetBytes));
    }
}

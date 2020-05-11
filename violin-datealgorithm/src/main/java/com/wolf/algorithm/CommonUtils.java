package com.wolf.algorithm;

/**
 * Description:
 *
 * @author 李超
 * @date 2020/01/09
 */
public class CommonUtils {

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}

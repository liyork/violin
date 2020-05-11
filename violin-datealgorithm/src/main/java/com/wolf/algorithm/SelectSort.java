package com.wolf.algorithm;

/**
 * Description: 选择排序(每次比较并选择，最后交换)
 * 经过第一轮比较后得到最小记录，将该记录与第一个记录交换
 * 对不包括第一个记录的剩余元素进行第二轮比较，得到最小与第二个记录交换
 * 重复以上，直到比较的记录只有一个时为止
 *
 * @author 李超
 * @date 2020/01/01
 */
public class SelectSort {

    public static void main(String[] args) {
        int[] arr = {5, 4, 2, 3, 1};

        int length = arr.length;
        for (int i = 0; i < length; i++) {// 循环n边，每次将最小值与i位置交换

            int minIndex = i;
            int minValue = arr[minIndex];
            for (int j = i + 1; j < length; j++) {// 从i~length寻找一个最小index
                if (minValue > arr[j]) {
                    minIndex = j;
                    minValue = arr[j];
                }
            }

            // 将最小index与i交换
            if (i != minIndex) {
                arr[minIndex] = arr[i];
                arr[i] = minValue;
            }
        }

        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}

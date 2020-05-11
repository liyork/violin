package com.wolf.algorithm;

/**
 * Description: 冒泡排序(每次比较并交换)
 * 从第一个元素开始到最后一个元素，比较临近元素将大的放到右边，第一轮得到最大值位于最后位置
 * 从第一个元素开始到倒数第一个元素，，比较临近元素将大的放到右边，第二轮得到最大值位于倒数第二位置
 * 重复以上，直到比较的记录只有一个时为止
 *
 * @author 李超
 * @date 2020/01/01
 */
public class BubblingSort {

    public static void main(String[] args) {
        int[] arr = {5, 4, 2, 3, 1};

        int length = arr.length;
        for (int endIndex = length - 1; endIndex > 0; endIndex--) {// end从length-1位置开始，直到1
            int tmp;
            for (int i = 0; i < endIndex; i++) {// 每次从0~end比较相邻，得到一个大值
                if (arr[i] > arr[i + 1]) {
                    tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;
                }
            }
        }

        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}

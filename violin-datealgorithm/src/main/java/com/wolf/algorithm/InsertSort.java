package com.wolf.algorithm;

import org.junit.Test;

/**
 * Description:插入排序
 * 第一个元素是有序序列，用第二个元素插入左边序列位置
 * 第三个元素插入左边有序位置。
 * 重复以上，直到最后一个元素插入完毕
 * <p>
 * 平均时间复杂度：n^2，外层需要遍历前n-1个数据找到位置，内层需要遍历进行进行移动数组
 * <p>
 * 空间复杂度：O(1)  (用于记录需要插入的数据)，即sortedValue
 * <br/> Created on 2017/6/5 21:54
 *
 * @author 李超
 * @since 1.0.0
 */
public class InsertSort {

    public static void main(String[] args) {

        int[] arr = {8, 5, 2, 0, 7, 3, 9, 1};// 无重复
//        int[] arr = {5, 8, 2, 0, 5, 7, 3, 8, 9, 1};// 有重复

        insertSort1(arr);
//        insertSort2(arr);
//        insertSort3(arr);

        for (int i : arr) {
            System.out.print(i + " ");
        }
    }

    // 遇到大于当前值的直接移动
    private static void insertSort1(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int firstGreaterThanLeftIndex = i;
            int insertValue = arr[firstGreaterThanLeftIndex];
            //找到第一个前面数值比他小的
            while (firstGreaterThanLeftIndex >= 1 && arr[firstGreaterThanLeftIndex - 1] > insertValue) {
                arr[firstGreaterThanLeftIndex] = arr[firstGreaterThanLeftIndex - 1];
                firstGreaterThanLeftIndex--;
            }

            if (firstGreaterThanLeftIndex != i) {// 如果没有移动则不进行赋值
                arr[firstGreaterThanLeftIndex] = insertValue;
            }
        }
    }

    // 记住开始移动点，然后移动数组
    private static void insertSort2(int[] arr) {

        for (int i = 1; i < arr.length; i++) {// 每次遍历都认为i前面的都是已经排序好的
            int insertValue = arr[i];// 待插入元素

            int compareIndex = i - 1;
            // 从i-1位置开始，找到第一个比i小的元素位置
            while (compareIndex >= 0) {
                if (insertValue > arr[compareIndex]) {
                    break;
                }
                compareIndex--;
            }

            int insertIndex = compareIndex + 1;// 从上面找到元素之后开始移动
            if (i != insertIndex) {
                moveElement(arr, i - 1, insertIndex, insertValue);
            }
        }
    }

    // 从startIndex开始将元素向后移动，直到endIndex，最后放入endIndex为
    private static void moveElement(int[] arr, int startIndex, int endIndex, int insertValue) {
        for (; startIndex >= endIndex; startIndex--) {
            arr[startIndex + 1] = arr[startIndex];
        }
        arr[endIndex] = insertValue;
    }


    /**
     * 基于快速排序，二分插入
     *
     * @param arr
     */
    private static void insertSort3(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int sortedValue = arr[i];
            //找到前面数值第一个比他小的，所在位置是小的右边
            int firstGreaterThanLeftIndex = binarySearch(arr, 0, i, sortedValue);

            moveElement(arr, i, firstGreaterThanLeftIndex, sortedValue);
        }
    }

    /**
     * 二分查找
     *
     * @param arr
     * @param low
     * @param high
     * @param searchValue
     * @return 要放入的位置
     */
    private static int binarySearch(int[] arr, int low, int high, int searchValue) {
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] < searchValue) {
                low = mid + 1;//经过试验，是mid+1和mid-1，得到结果这个好
            } else {
                high = mid;
            }
        }

        assert low == high;
        return low;
    }

    @Test
    public void testBinarySearch() {
        int[] arr1 = {1, 2, 3, 4, 5, 6, 8, 9};
        int i = binarySearch(arr1, 0, arr1.length - 1, 7);
        System.out.println(i);

    }

}

package com.wolf.algorithm;

/**
 * Description:插入排序，基于要插入的序列（从0开始）是已经排序好的。
 * 平均时间复杂度：n^2，外层需要遍历前n-1个数据找到位置，内层需要遍历进行进行移动数组
 *
 * 空间复杂度：O(1)  (用于记录需要插入的数据)，即sortedValue
 * <br/> Created on 2017/6/5 21:54
 *
 * @author 李超
 * @since 1.0.0
 */
public class InsertSort {

    public static void main(String[] args) {

//        int[] arr = {8, 5, 2, 0, 7, 3, 9, 1};
        int[] arr = {5, 8, 2, 0, 5, 7, 3, 8, 9, 1};

//        insertSort1(arr);//1种
//        insertSort2(arr);//2种


//        int[] arr1 = {1, 2, 3, 4, 5, 6, 8, 9};
//        binarySearch(arr1, 0, arr1.length - 1, 7);

        insertSort3(arr);//3种

        for(int i : arr) {
            System.out.print(i + " ");
        }
    }

    /**
     * 记住开始移动点，然后移动数组
     *
     * @param arr
     */
    private static void insertSort1(int[] arr) {
        //每次遍历都认为i前面的都是已经排序好的
        for(int i = 1; i < arr.length; i++) {
            int toInsertIndex = i;
            int sortedValue = arr[toInsertIndex];
            //找到第一个从i-1开始的数值比sortedValue小的
            while(toInsertIndex >= 1 && arr[toInsertIndex - 1] > sortedValue) {
                toInsertIndex--;
            }

            moveElement(arr, toInsertIndex, i, sortedValue);
        }
    }

    /**
     * 倒序移动
     * 例如：1342,要移动34然后放入2
     * startIndex = 3
     * endIndex=1
     * sortedValue = 2
     *
     * @param arr
     * @param startIndex
     * @param endIndex
     * @param sortedValue
     */
    private static void moveElement(int[] arr, int startIndex, int endIndex, int sortedValue) {
        for(; startIndex > endIndex; startIndex--) {
            arr[startIndex] = arr[startIndex - 1];
        }
        arr[startIndex] = sortedValue;
    }

    /**
     * 遇到大于当前值的直接移动
     *
     * @param arr
     */
    private static void insertSort2(int[] arr) {
        for(int i = 1; i < arr.length; i++) {
            int firstGreaterThanLeftIndex = i;
            int sortedValue = arr[firstGreaterThanLeftIndex];
            //找到前面数值第一个比他小的
            while(firstGreaterThanLeftIndex >= 1 && arr[firstGreaterThanLeftIndex - 1] > sortedValue) {
                arr[firstGreaterThanLeftIndex] = arr[firstGreaterThanLeftIndex - 1];
                firstGreaterThanLeftIndex--;
            }
            //如果没有移动则不进行赋值
            if(firstGreaterThanLeftIndex != i) {
                arr[firstGreaterThanLeftIndex] = sortedValue;
            }
        }
    }

    /**
     * 基于快速排序，二分插入
     *
     * @param arr
     */
    private static void insertSort3(int[] arr) {
        for(int i = 1; i < arr.length; i++) {
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
        while(low < high) {
            int mid = (low + high) / 2;
            if(arr[mid] < searchValue) {
                low = mid + 1;//经过试验，是mid+1和mid-1，得到结果这个好
            } else {
                high = mid;
            }
        }

        assert low == high;
        return low;
    }

}

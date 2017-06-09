package com.wolf.algorithm;

/**
 * Description:
 * <br/> Created on 2017/6/6 14:31
 *
 * @author 李超
 * @since 1.0.0
 */
public class BinarySearch {

    public static void main(String[] args) {

        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int i = binarySearch(arr, 0, arr.length - 1, 7);
        System.out.println(i);
    }

    private static int binarySearch(int[] arr, int low, int high, int searchValue) {
        while(low < high) {
            int mid = (low + high) / 2;
            if(searchValue == arr[mid]) {
                return mid;
            } else if(searchValue < arr[mid]) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return low;
    }

}

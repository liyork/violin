package com.wolf.algorithm;

/**
 * Description: 希尔排序，缩小增量排序，实现跳跃式移动。
 * 先将数组元素按照指定步长连接多个间隔相连的序列，对其插入排序。
 * 再缩小步长，重复上述过程。
 * 最后对所有元素(基本有序)来一次步长为1的插入排序。
 *
 * @author 李超
 * @date 2020/01/01
 */
public class ShellSort {

    public static void main(String[] args) {

        int[] arr = {6, 8, 5, 9, 3, 4, 7};

        int length = arr.length;
        int i, j;
        int step = length / 2;
        int insertValue;
        while (step > 0) {// 从length/2开始每次除2，最后一次为1
            for (i = step; i < length; i++) {// 从step开始，插入之前的step序列，为一轮，下次+1
                insertValue = arr[i];
                for (j = i - step; j >= 0; j -= step) {
                    if (insertValue < arr[j]) {// insertValue要放入之前序列的正确位置(比左边元素大的位置)。所以小于则移动。
                        arr[j + step] = arr[j];
                    } else {
                        break;
                    }
                }
                arr[j + step] = insertValue;
            }
            step = step / 2;
        }

        for (int i1 : arr) {
            System.out.print(i1 + " ");
        }
    }
}

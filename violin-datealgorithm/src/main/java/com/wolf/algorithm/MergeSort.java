package com.wolf.algorithm;

import org.junit.Test;

import java.util.Stack;

/**
 * Description: 归并排序
 * 利用递归与分治将洗了划分为更多的子序列，不能再分时(1元素——有序)，再对子序列合并(有序)，然后再对更大子序列合并。
 * <p>
 * 递归情况：
 * 1. 总过程需要进行logn趟。每一趟归并排序操作，就是将两个有序子序列进行归并，数据的比较次数均小于等于数据的移动次数，
 * 数据移动次数均等于整体数据的个数n，即每一趟归并的时间复杂度为O(n)。
 * 因此归并排序时间复杂度为O(nlogn)
 * 2. 空间复杂度：n，需要额外一个数组进行拷贝
 * 3. 稳定，由于细化到最小2个单元进行比较，所以左边的数永远在左边
 * <br/> Created on 2017/6/6 15:44
 *
 * @author 李超
 * @since 1.0.0
 */
public class MergeSort {

    private static void mergeSort1(int[] arr, int lowIndex, int highIndex) {
        if (lowIndex >= highIndex) {
            return;
        }
        int midIndex = (highIndex + lowIndex) / 2;
//        mergeSort(arr, lowIndex, midIndex-1);//9/2=4，已经等分了,如果把mid给右边，则他就少了
        mergeSort1(arr, lowIndex, midIndex);
        mergeSort1(arr, midIndex + 1, highIndex);
        combine(arr, lowIndex, midIndex + 1, highIndex);
    }

    // 将数组的lowIndex~midIndex与midIndex+1~highIndex合并排序放入到新数组中，然后再拷贝到旧数组相应位置
    private static void combine(int[] arr, int lowIndex, int midIndex, int highIndex) {
        int num = (highIndex - lowIndex) + 1;
        int[] tmpArr = new int[num];

        int leftIndex = lowIndex;
        int rightIndex = midIndex;
        int i = 0;
        boolean isLeftResidue = false;

        while (true) {
            int leftValue;
            if (leftIndex <= (midIndex - 1)) {
                leftValue = arr[leftIndex];
            } else {
                break;
            }

            int rightValue;
            if (rightIndex <= highIndex) {
                rightValue = arr[rightIndex];
            } else {
                isLeftResidue = true;
                break;
            }

            int tempValue;
            if (leftValue < rightValue) {
                tempValue = leftValue;
                leftIndex++;
            } else {
                tempValue = rightValue;
                rightIndex++;
            }

            tmpArr[i++] = tempValue;
        }

        if (isLeftResidue) {// 左边还有剩余
            for (; i < num; i++) {
                tmpArr[i] = arr[leftIndex++];
            }
        } else {// 右边还有剩余
            for (; i < num; i++) {
                tmpArr[i] = arr[rightIndex++];
            }
        }

        // 写入源数组
        for (int x = 0, j = lowIndex; j <= highIndex; j++) {
            arr[j] = tmpArr[x++];
        }
    }

    /**
     * 在mergeSort1的基础上优化，使用一个整体的临时数组，避免每次都创建小的数据带来内存碎片和new的开销
     *
     * @param arr
     * @param lowIndex
     * @param highIndex
     * @param tempArr
     */
    private static void mergeSort2(int[] arr, int lowIndex, int highIndex, int[] tempArr) {
        if (lowIndex >= highIndex) {
            return;
        }

        int midIndex = (highIndex + lowIndex) / 2;
        mergeSort2(arr, lowIndex, midIndex, tempArr);
        mergeSort2(arr, midIndex + 1, highIndex, tempArr);
        combine2(arr, lowIndex, midIndex, highIndex, tempArr);
    }

    // index所在范围都是包含
    private static void combine2(int[] arr, int lowIndex, int midIndex, int highIndex, int[] tempArr) {

        int leftIndex = lowIndex;
        int rightIndex = midIndex + 1;
        int i = 0;// 单线程，从0开始没问题

        while (leftIndex <= midIndex && rightIndex <= highIndex) {
            int leftValue = arr[leftIndex];
            int rightValue = arr[rightIndex];
            int tempValue;
            if (leftValue < rightValue) {
                tempValue = leftValue;
                leftIndex++;
            } else {
                tempValue = rightValue;
                rightIndex++;
            }
            tempArr[i++] = tempValue;
        }

        int num = (highIndex - lowIndex) + 1;
        while (i < num) {
            if (leftIndex <= midIndex) {
                tempArr[i++] = arr[leftIndex++];
            } else {
                tempArr[i++] = arr[rightIndex++];
            }
        }

        // 写入源数组
        int x = 0;
        int tempLowIndex = lowIndex;//不想污染low变量
        while (tempLowIndex <= highIndex) {
            arr[tempLowIndex++] = tempArr[x];
            tempArr[x] = 0;
            x++;
        }
    }

    // 非递归归并排序————用栈模拟
    private static void mergeSortNoRecursive1(int[] arr, int low, int high) {

        int[] tempArr = new int[arr.length];
        Stack<int[]> stack = new Stack<>();

        // 初始化
        splitAndPush(low, high, stack);

        while (stack.size() != 1) {

            int[] arrPart1 = stack.pop();
            boolean isSorted1 = arrPart1[0] == 1;
            int leftIndex1 = arrPart1[1];
            int rightIndex1 = arrPart1[2];
            int num = rightIndex1 - leftIndex1 + 1;
            if (!isSorted1) {//未排
                // 3个被拆分成2和1个，1是已排序。所以不会有num=1场景
                if (num > 2) {
                    //拆，放入
                    splitAndPush(leftIndex1, rightIndex1, stack);
                } else if (num == 2) {
                    //排序放入
                    int midIndex = (rightIndex1 + leftIndex1) / 2;
                    combine2(arr, leftIndex1, midIndex, rightIndex1, tempArr);
                    arrPart1[0] = 1;
                    stack.push(arrPart1);// 再次放入已排序好的，用于下次与另一半进行合并
                }
            } else {//已排
                int[] arrPart2 = stack.pop();

                boolean isSorted2 = arrPart2[0] == 1;
                if (!isSorted2) {//未排
                    stack.push(arrPart1);//排好的入栈
                    stack.push(arrPart2);
                } else {
                    //合并,放入
                    int leftIndex2 = arrPart2[1];
                    int rightIndex2 = arrPart2[2];
                    // 由于上面发现arrPart2未排序先把arrPart2放入然后放入arrPart1，之后arrPart2排序好后，再到这里时，
                    // arrPart1和arrPart2就反了，需要判断一下取较小的下标和较大的上标
                    int tempLowIndex = leftIndex1 < leftIndex2 ? leftIndex1 : leftIndex2;
                    int tempHighIndex = rightIndex1 > rightIndex2 ? rightIndex1 : rightIndex2;
                    int tempMidIndex = (tempLowIndex + tempHighIndex) / 2;
                    combine2(arr, tempLowIndex, tempMidIndex, tempHighIndex, tempArr);
                    arrPart2[0] = 1;
                    arrPart2[1] = tempLowIndex;
                    arrPart2[2] = tempHighIndex;
                    stack.push(arrPart2);
                }
            }
        }

    }

    // 非递归归并排序————二路合并，增长步长合并
    private static void mergeSortNoRecursive2(int[] arr) {
        int length = arr.length;
        int[] tempArr = new int[length];

        int step = 2;
        //按照步长进行二路合并，至少要满足一个步长
        while (step <= length) {
            int start = 0;
            while (start + step <= length) {
                int endIndex = start + step - 1;
                int mid = (start + endIndex) / 2;
//                int x = start + step / 2 - 1;// 与mid值一样
                combine2(arr, start, mid, start + step - 1, tempArr);
                start += step;
            }
            // 合并start+step>length不处理的剩余部分
            int endIndex = start + step - 1;
            int mid = (start + endIndex) / 2;
//            int x = start + step / 2 - 1;// 与mid值一样
            combine2(arr, start, mid, length - 1, tempArr);

            step = step * 2;
        }

        //合并0~length-1,从头到尾处理一遍
        int midIndex = step / 2 - 1;// 之前步长都是2^n次进行归并，剩余部分补充归并，最后这次用step/2-1不会重复
        combine2(arr, 0, midIndex, length - 1, tempArr);
    }

    /**
     * 由于递归一般是先处理左边的，而栈结构是后进先出，所以先放右边的
     * pop[0] 是否已经排序过
     * pop[1] 开始下标
     * pop[2] 结束下标
     *
     * @param lowIndex
     * @param highIndex
     * @param stack
     */
    private static void splitAndPush(int lowIndex, int highIndex, Stack<int[]> stack) {
        int mid = (highIndex + lowIndex) / 2;

        int[] rightArr = new int[3];
        int rightIndex = mid + 1;
        int rightNum = highIndex - rightIndex + 1;
        rightArr[0] = rightNum == 1 ? 1 : 0;// 1排序，0未排序
        rightArr[1] = rightIndex;
        rightArr[2] = highIndex;
        stack.push(rightArr);

        int[] leftArr = new int[3];
        int leftNum = mid - lowIndex + 1;
        leftArr[0] = leftNum == 1 ? 1 : 0;
        leftArr[1] = lowIndex;
        leftArr[2] = mid;
        stack.push(leftArr);
    }

    @Test
    public void testMereSort1() {
        int[] arr = {7, 2, 8, 9, 3};
        mergeSort1(arr, 0, arr.length - 1);
        print(arr);
    }

    @Test
    public void testMereSort2() {
        int[] arr = {7, 2, 8, 9, 3};
        int[] tempArr = new int[arr.length];
        mergeSort2(arr, 0, arr.length - 1, tempArr);
        print(arr);
    }

    @Test
    public void testMergeSortNoRecursive1() {
//        int[] arr = {7, 2, 8, 9, 3};
        int[] arr = {5, 8, 2, 4, 3, 9, 1, 8, 6, 7, 11};
        mergeSortNoRecursive1(arr, 0, arr.length - 1);
        print(arr);
    }

    @Test
    public void testMergeSortNoRecursive2() {
        int[] arr = {5, 8, 2, 4, 3, 9, 1, 8, 6, 7, 11};
//        int[] arr = {7, 2, 8, 9, 3, 5};
        mergeSortNoRecursive2(arr);
        print(arr);
    }


    private void print(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }

}

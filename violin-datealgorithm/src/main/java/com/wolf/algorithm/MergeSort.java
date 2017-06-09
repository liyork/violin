package com.wolf.algorithm;

import java.util.Stack;

/**
 * Description:
 * 归并排序
 * 时间复杂度：合并n*分治log2n
 * 空间复杂度：n，需要额外一个数组进行拷贝
 * 稳定，由于细化到最小2个单元进行比较，所以左边的数永远在左边
 * <br/> Created on 2017/6/6 15:44
 *
 * @author 李超
 * @since 1.0.0
 */
public class MergeSort {

    public static void main(String[] args) {

//        int[] arr = {5, 8, 2, 0, 5, 7, 3, 8, 9, 1};
//        mergeSort(arr, 0, arr.length - 1);

//        int[] tempArr = new int[arr.length];
//        mergeSort2(arr, 0, arr.length - 1, tempArr);

//        mergeSort3(arr, 0, arr.length - 1, tempArr);

//        int[] arr1 = {4, 2, 6, 1, 3};
//        mergeSortNoRecursive1(arr, 0, arr.length - 1);

        //        int[] arr = {5, 8, 2, 4, 3};
//        int[] arr = {5, 8, 2, 4, 3, 9, 1, 8, 6, 7, 11};
//        mergeSortOnline(arr);

        int[] arr = {5, 8, 2, 4, 3, 9, 1, 8, 6, 7, 11};
        mergeSortNoRecursive2(arr);

        for(int i : arr) {
            System.out.print(i + " ");
        }
    }

    private static void mergeSort(int[] arr, int low, int high) {
        //原先判断，没有什么意义，不如放到方法中
//        int num = (high - low) + 1;
//        if(num == 1) {
//            return;
//        }
        if(low == high) {
            return;
        }
        int mid = (high + low) / 2;
//        mergeSort(arr, low, mid-1);//9/2=4，已经等分了,如果把mid给右边，则他就少了
        mergeSort(arr, low, mid);
        mergeSort(arr, mid + 1, high);
        combine(arr, low, mid, high);
    }

    /**
     * 将数组的low---mid-1   mid----high  合并拷贝到新数组中，然后再拷贝回来
     *
     * @param arr
     * @param low
     * @param mid
     * @param high
     */
    private static void combine(int[] arr, int low, int mid, int high) {
        int num = (high - low) + 1;
        int[] arrCopy = new int[num];

        int lowLeft = low;
        int highLeft = mid + 1;
        int i = 0;
        for(; i < num; i++) {
            int lowLeftValue;
            if(lowLeft <= mid) {
                lowLeftValue = arr[lowLeft];
            } else {
                break;
            }

            int highLeftValue;
            if(highLeft <= high) {
                highLeftValue = arr[highLeft];
            } else {
                break;
            }

            int tempValue;
            if(lowLeftValue < highLeftValue) {
                tempValue = lowLeftValue;
                lowLeft++;
            } else {
                tempValue = highLeftValue;
                highLeft++;
            }

            arrCopy[i] = tempValue;
        }

        if(lowLeft <= mid) {
            for(int q = i; q < num; q++) {
                arrCopy[q] = arr[lowLeft++];
            }
        } else {
            for(int q = i; q < num; q++) {
                arrCopy[q] = arr[highLeft++];
            }
        }

        //这里要新定义个index，由于arrCopy只是当前递归的容量
        int x = 0;
        for(int j = low; j <= high; j++) {
            arr[j] = arrCopy[x++];
        }
    }


    /**
     * 在mergeSort1的基础上优化，使用一个整体的临时数组，避免每次都创建小的数据带来内存碎片和new的开销
     *
     * @param arr
     * @param low
     * @param high
     * @param tempArr
     */
    private static void mergeSort2(int[] arr, int low, int high, int[] tempArr) {
        //原先判断，没有什么意义，不如放到方法中
//        int num = (high - low) + 1;
//        if(num == 1) {
//            return;
//        }
        if(low == high) {
            return;
        }

        int mid = (high + low) / 2;
//        mergeSort(arr, low, mid-1);//9/2=4，已经等分了,如果把mid给右边，则他就少了
        mergeSort2(arr, low, mid, tempArr);
        mergeSort2(arr, mid + 1, high, tempArr);
        combine2(arr, low, mid, high, tempArr);
    }

    /**
     * 在mergeSort2的基础上优化，优化combine3
     *
     * @param arr
     * @param low
     * @param high
     * @param tempArr
     */
    private static void mergeSort3(int[] arr, int low, int high, int[] tempArr) {
        if(low == high) {
            return;
        }

        int mid = (high + low) / 2;
        mergeSort3(arr, low, mid, tempArr);
        mergeSort3(arr, mid + 1, high, tempArr);
        combine3(arr, low, mid, high, tempArr);
    }

    /**
     * 将数组的low---mid-1   mid----high  合并拷贝到新数组中，然后再拷贝回来
     *
     * @param arr
     * @param low
     * @param mid
     * @param high
     */
    private static void combine2(int[] arr, int low, int mid, int high, int[] tempArr) {
        int num = (high - low) + 1;

        int lowLeft = low;
        int highLeft = mid + 1;
        int i = 0;
        for(; i < num; i++) {
            int lowLeftValue;
            if(lowLeft <= mid) {
                lowLeftValue = arr[lowLeft];
            } else {
                break;
            }

            int highLeftValue;
            if(highLeft <= high) {
                highLeftValue = arr[highLeft];
            } else {
                break;
            }

            int tempValue;
            if(lowLeftValue < highLeftValue) {
                tempValue = lowLeftValue;
                lowLeft++;
            } else {
                tempValue = highLeftValue;
                highLeft++;
            }

            tempArr[i] = tempValue;
        }

        if(lowLeft <= mid) {
            for(int q = i; q < num; q++) {
                tempArr[q] = arr[lowLeft++];
            }
        } else {
            for(int q = i; q < num; q++) {
                tempArr[q] = arr[highLeft++];
            }
        }

        //这里要新定义个index，由于tempArr只是当前递归的容量
        int x = 0;
        for(int j = low; j <= high; j++) {
            arr[j] = tempArr[x++];
        }
    }

    /**
     * 优化combine2代码，去掉最外层的for循环，没有意义，因为两个数组肯定有一个先执行完
     *
     * @param arr
     * @param low
     * @param mid
     * @param high
     */
    private static void combine3(int[] arr, int low, int mid, int high, int[] tempArr) {

        if(low >= high) {
            return;
        }

        int lowLeft = low;
        int highLeft = mid + 1;
        //从第0个位置放tempArr
        int i = 0;

        while(lowLeft <= mid && highLeft <= high) {
            int lowLeftValue = arr[lowLeft];
            int highLeftValue = arr[highLeft];
            int tempValue;
            if(lowLeftValue < highLeftValue) {
                tempValue = lowLeftValue;
                lowLeft++;
            } else {
                tempValue = highLeftValue;
                highLeft++;
            }
            tempArr[i++] = tempValue;
        }

        int num = (high - low) + 1;
        while(i < num) {
            if(lowLeft <= mid) {
                tempArr[i++] = arr[lowLeft++];
            } else {
                tempArr[i++] = arr[highLeft++];
            }
        }

        int x = 0;
        int tempLow = low;//不想污染low变量
        while(tempLow <= high) {
            arr[tempLow++] = tempArr[x++];
        }
    }

    /**
     * 非递归归并排序，模拟栈结构操作
     *
     * @param arr
     * @param low
     * @param high
     */
    private static void mergeSortNoRecursive1(int[] arr, int low, int high) {

        int[] tempArr = new int[arr.length];
        Stack<int[]> stack = new Stack<>();

        splitAndPush(low, high, stack);

        while(true) {
            if(stack.size() == 1) {
                break;
            }

            int[] pop = stack.pop();
            int isSorted = pop[0];
            int lowIndex = pop[1];
            int highIndex = pop[2];
            int num = highIndex - lowIndex + 1;
            if(isSorted == 0) {//未排
                if(num > 2) {
                    //拆，放入
                    splitAndPush(lowIndex, highIndex, stack);
                } else if(num == 2) {
                    //排序放入
                    int midIndex = (highIndex + lowIndex) / 2;
                    combine2(arr, lowIndex, midIndex, highIndex, tempArr);
                    pop[0] = 1;
                    stack.push(pop);
                }
            } else {//已排
                int[] pop2 = stack.pop();

                int isSorted2 = pop2[0];
                if(isSorted2 == 0) {//未排
                    stack.push(pop);//拍好的入栈
                    stack.push(pop2);
                } else {
                    //合并,放入
                    int lowIndex2 = pop2[1];
                    int highIndex2 = pop2[2];
                    //由于放入时为了维护先左出再右出，这里需要判断一下取较小的下标较大的上标
                    int tempLowIndex = lowIndex < lowIndex2 ? lowIndex : lowIndex2;
                    int tempHighIndex = highIndex > highIndex2 ? highIndex : highIndex2;
                    int tempMidIndex = (tempLowIndex + tempHighIndex) / 2;
                    combine2(arr, tempLowIndex, tempMidIndex, tempHighIndex, tempArr);
                    pop2[0] = 1;
                    pop2[1] = tempLowIndex;
                    pop2[2] = tempHighIndex;
                    stack.push(pop2);
                }
            }
        }

    }

    /**
     * 非递归归并排序，二路合并
     *
     * @param arr
     */
    private static void mergeSortNoRecursive2(int[] arr) {
        int length = arr.length;
        int[] tempArr = new int[length];

        int step = 2;
        //按照步长进行二路合并，至少要满足一个步长
        while(step <= length) {
            int start = 0;
            //每个合并的开始索引
            while(start + step <= length) {
                int mid = (start + (start + step) - 1) / 2;//中间索引=(lowIndex+highIndex)/2
                combine3(arr, start, mid, start + step - 1, tempArr);
                start = start + step;
            }
            int mid = (start + (start + step) - 1) / 2;
            combine3(arr, start, mid, length - 1, tempArr);
            step = step * 2;
        }
        //合并0---上次步长---length-1
        combine3(arr, 0, step / 2, length - 1, tempArr);
    }

    /**
     * 由于递归一般是先处理左边的，而栈结构是后进先出，所以先放右边的
     * pop[0] 是否已经排序过
     * pop[1] 开始下标
     * pop[2] 结束下标
     *
     * @param low
     * @param high
     * @param stack
     */
    private static void splitAndPush(int low, int high, Stack<int[]> stack) {
        int mid = (high + low) / 2;

        int[] rightArr = new int[3];
        int rightArrLeftIndex = mid + 1;
        int rightArrNum = high - rightArrLeftIndex + 1;
        rightArr[0] = rightArrNum == 1 ? 1 : 0;
        rightArr[1] = rightArrLeftIndex;
        rightArr[2] = high;
        stack.push(rightArr);

        int[] leftArr = new int[3];
        int leftArrNum = mid - low + 1;
        leftArr[0] = leftArrNum == 1 ? 1 : 0;
        leftArr[1] = low;
        leftArr[2] = mid;
        stack.push(leftArr);
    }


    public static void mergeSortOnline(int[] data) {
        int[] tempArr = new int[data.length];

        int n = data.length;
        //步长
        int s = 2;
        int i;
        while(s <= n) {
            i = 0;
            while(i + s <= n) {
                //这个中间值为什么这么算？？i+步长的一半再-1
                combine3(data, i, i + s / 2 - 1, i + s - 1, tempArr);
                i += s;
            }
            //处理末尾残余部分
            combine3(data, i, i + s / 2 - 1, n - 1, tempArr);
            s *= 2;
        }
        //最后再从头到尾处理一遍
        combine3(data, 0, s / 2 - 1, n - 1, tempArr);
    }


}

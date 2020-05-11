package com.wolf.algorithm;

/**
 * Description: 快速排序，采用分治思想，拆分大为小。
 * 通过一趟排序后，将原序列分为两部分，前部分所有数据比后部分的数据小，递归该过程对前后部分各自快速排序，直到只有一个元素力度，即为有序了。
 * 一般以第一个元素为关键字划分。
 * 注：当初始序列整体或局部有序时，快速排序性能下降，退化为冒泡排序。
 * <p>
 * 特点：
 * 1. 最坏时间复杂度
 * 指每次区间划分的结果都是基准关键字的左边(或右边)序列为空，导致另一边区间中的记录项仅比排序前少了一项，即基准关键字是待排序的所有记录中
 * 最小或最大的。
 * 如：当初始序列按递增顺序排列时，若选取第一个记录为基准关键字，则每次选择的都是所有记录中最小者，这时记录与基准关键字的比较次数会增多。因此
 * 需要进行(n-1)次区间划分。
 * 对于第k(0<k<n)次区间划分，划分前的序列长度为(n-k+1)，需要进行(n-k)次记录的比较。因此，当k从1到n-1时，进行的比较次数总共为n(n-1)/2。
 * 所以最坏时间复杂度为O(n^2)
 * 2. 最好时间复杂度
 * 指每次区间划分的结果都是基准关键字左右两边的序列长度相等或相差1，即基准关键字为待排序的记录中的中间值。此时进行比较次数总共为nlogn。
 * 所以最好时间复杂度为O(nlogn)
 * 3. 平均时间复杂度
 * 是O(nlogn)，虽然最坏为O(n^2)，但在所有平均时间复杂度为O(nlogn)的算法中，快排的平均性能是最好的。
 * 4. 空间复杂度
 * 快排过程中需要一个栈空间来实现递归。当每次划分比较均匀时(即最好情况),递归树的最大深度为logn+1(logn为向上取整)。当每次区间划分都
 * 使得一边的序列长度为0时(即最坏情况),递归树的最大深度为n。
 * 在每轮排序结束后比较基准关键字左右的记录个数，对记录多的一边先排序，此时栈的最大深度可降为logn。
 * 因此，快速排序的平均空间复杂度为O(logn)
 * 5. 基准关键字的选取
 * a. 三者取中。指在当前序列中，首、尾和中间位置上记录比较，选择中值，划分开始前交换序列中的第一个记录与基准关键字位置。
 * b. 取随机数。称为随机的快速排序。
 * 6. 不稳定
 * 27 25 27 3，已第一个27为中心，将3和第一个27交换得到  3 25 27 27，这个不稳定了
 * 7. 与归并排序
 * 相同点：原理都是分治思想，即先把待排序的元素分成两组，然后分别对这两组排序，最后把两组结果合并。
 * 不同点：进行的分组策略不同，后面的合并策略也不同。
 * 归并排序的分组策略是按元素大小分组，即数组前后一半分组。快速排序是根据元素值来分组，即大于某个值的元素放在一组，而小于的放另一组。
 * 总的来说，若分组策略越简单，则后面的合并策略越复杂，因为快排在分组时，已经根据元素大小来分组了，而合并时只需把两个分组合并起来就行，
 * 归并排序则需要对两个有序的数组根据大小合并。
 * <br/> Created on 2017/5/2 18:22
 *
 * @author 李超
 * @since 1.0.0
 */
public class QuickSort {

    //todo-my 稍后查看
    public void quickSort3(int[] arr, int left, int right) {

        if (left >= right) {
            return;
        }

        int tempDate;
        int leftIndex = left;
        int rightIndex = right;
        int middle = arr[(leftIndex + rightIndex) / 2];

        // 一次循环完后，middle左边的都比右边的值小
        do {
            while (arr[leftIndex] < middle && leftIndex < right) {//找出左边比中间值大于等于的数
                leftIndex++;
            }

            while (arr[rightIndex] > middle && rightIndex > left) {//找出右边比中间值小于等于的数
                rightIndex--;
            }

            if (leftIndex <= rightIndex) { //将左边大的数和右边小的数进行替换
                if (leftIndex != rightIndex) {
                    tempDate = arr[leftIndex];
                    arr[leftIndex] = arr[rightIndex];
                    arr[rightIndex] = tempDate;
                }

                leftIndex++;
                rightIndex--;
            }
        } while (leftIndex < rightIndex); //当两者交错时停止

        // if (leftIndex < right) {
        quickSort3(arr, leftIndex, right);//排序右边
        // }

        //  if (rightIndex > left) {
        quickSort3(arr, left, rightIndex);//排序左边
        // }
    }

    //todo-my 稍后查看
    public void quickSort1(int[] strDate, int left, int right) {

        if (left >= right) {
            return;
        }

        int leftIndex = left;
        int rightIndex = right;
        int midValue = strDate[left];
        while (leftIndex < rightIndex) {

            while (leftIndex < rightIndex) {
                if (strDate[rightIndex] < midValue) {
                    strDate[leftIndex] = strDate[rightIndex];
                    leftIndex++;
                    break;
                }
                rightIndex--;
            }

            while (leftIndex < rightIndex) {
                if (strDate[leftIndex] > midValue) {
                    strDate[rightIndex] = strDate[leftIndex];
                    rightIndex--;
                    break;
                }
                leftIndex++;
            }
        }

        strDate[leftIndex] = midValue;

        quickSort1(strDate, left, leftIndex - 1);
        quickSort1(strDate, leftIndex + 1, right);
    }

    public void quickSort(int[] strDate, int left, int right) {

        if (left >= right) {
            return;
        }

        int midIndex = adjust(strDate, left, right);
        quickSort(strDate, left, midIndex - 1);// 中间值位置正确不用调整了
        quickSort(strDate, midIndex + 1, right);
    }

    //调整数组后，中间的值处于正确位置，左边的数小于中间值，右边的数大于中间值
    private int adjust(int[] arr, int left, int right) {
        int leftIndex = left;
        int rightIndex = right;
        int midValue = arr[left];

        while (leftIndex < rightIndex) {
            // 因为取mid用的left，这里先从右边找
            // 从右找第一个比midValue小的，可以越过中间值(因为有等于)只要小于leftIndex
            while (leftIndex < rightIndex && arr[rightIndex] >= midValue) {
                rightIndex--;
            }
            if (leftIndex < rightIndex) {
                arr[leftIndex++] = arr[rightIndex];// leftIndex作为mid已经没用了
            }

            //从左找第一个比temp大的
            while (leftIndex < rightIndex && arr[leftIndex] <= midValue) {
                leftIndex++;
            }
            if (leftIndex < rightIndex) {
                arr[rightIndex--] = arr[leftIndex];// 上面将rightIndex转走了，这里可以放到rightIndex位置。
            }
        }

        arr[leftIndex] = midValue;

        return leftIndex;
    }

    /**
     *   * @param args
     *  
     */
    public static void main(String[] args) {
//        int[] strVoid = new int[]{6, 8, 5, 9, 3, 4, 7};
        int[] strVoid = new int[]{7, 2, 8, 9, 3};
        QuickSort sort = new QuickSort();
        sort.quickSort(strVoid, 0, strVoid.length - 1);
        for (int i = 0; i < strVoid.length; i++) {
            System.out.println(strVoid[i] + " ");
        }
    }
}

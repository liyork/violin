package com.wolf.algorithm;

/**
 * Description: 堆排序
 * 堆是完全二叉树，大顶堆——每个结点的值都大于其左右孩子结点的值，反之为小顶堆。
 * 堆排序，利用堆数据结构来排序的选择排序。大顶堆，每次移除大元素，所以是升序。
 * <p>
 * 结点计算：从0开始，i的子节点为i*2+1和i*2+2，i的节点子节点为(i-1)/2
 * <p>
 * 平均时间复杂度：
 * 构建初始堆时间复杂度为O(n)。交换及重建大顶堆的过程中，需要执行n-1次(交换就确定一个最后位置，再重建又确定一个位置，最后n-1次时交换+重建即可到位)
 * 每次重建大顶堆的过程,根据完全二叉树的性质，[log2(n-1),log2(n-2)...1]逐步递减，近似为logn。
 * 所以最好和最坏的时间复杂度为O(nlogn)。
 * 空间复杂度为O(1)
 * <p>
 * 额外：
 * 0-->len/2-1相比len/2-1-->0，即从上开始调整还是从下开始调整。
 * 从上开始调整，到中间过程节点后，下面的节点可能会比上面大，所以还需要再向上调整。
 * 从下开始调整，每次调整后都能保证调整节点为最大，基础稳定，再向上就有保证，不用再考虑下面了。
 *
 * @author 李超
 * @date 2020/01/02
 */
public class HeapSort {

    public static void heapSort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }

        buildMaxHeap(arr);
        int endIndex = arr.length - 1;
        while (true) {
            CommonUtils.swap(arr, 0, endIndex);
            if (--endIndex == 0) {// 结尾是0下标直接返回，即使父元素是0，孩子最少也是1
                return;
            }
            heapify(arr, 0, endIndex);
        }
    }

    // 构造大根堆（自第一个有孩子的节点开始，每次不断向上比较交换）
    // 相比自底向上需要n次，自上向下次数少了
    public static void buildMaxHeap(int[] arr) {
        int length = arr.length;
        // 从length/2-1位置(从尾部开始向前查找,第一个有孩子的父节点)开始，直到0(根节点)。
        for (int i = length / 2 - 1; i >= 0; i--) {
            heapify(arr, i, length - 1);
        }
    }

    // 比较当前值和孩子，若自己大则完成。否则找大的交换，然后向下，直到符合堆特性或者不变或者到尾。
    public static void heapify(int[] arr, int parentIndex, int endIndex) {
        int leftIndex = getLeftChildIndex(parentIndex);
        int rightIndex = getRightChildIndex(parentIndex);

        int maxIndex = parentIndex;
        while (true) {
            // 左右都比较，选一个大的，这样提上来另一个也满足条件
            if (leftIndex <= endIndex && arr[leftIndex] > arr[maxIndex]) {
                maxIndex = leftIndex;
            }

            if (rightIndex <= endIndex && arr[rightIndex] > arr[maxIndex]) {
                maxIndex = rightIndex;
            }

            if (parentIndex == maxIndex) {
                break;
            }

            CommonUtils.swap(arr, maxIndex, parentIndex);
            parentIndex = maxIndex;

            leftIndex = getLeftChildIndex(parentIndex);
            rightIndex = getRightChildIndex(parentIndex);
        }

    }

    private static int getRightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    private static int getLeftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    public static void main(String[] args) {
        int[] arr = {7, 2, 8, 9, 3, 5, 6};
        heapSort(arr);

        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}

package com.wolf.algorithm;

/**
 * <p> Description:桶排序，先将各元素划分到桶中，然后将同种元素排序，分开排序，最后顺序输出数组。一般适用于关键字取值范围较小情况，
 * 否则所需桶数据太多导致浪费存储空间和计算时间。
 * 平均时间复杂度：O(n)，最坏为O(n^2)
 * 假设输入的待排序元素是等可能的落在等间隔的值区间内
 * <p/>
 * Date: 2015/12/24
 * Time: 20:13
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class BucketSort {

    private static void bucketSort(int[] keys, int bucketSize) {
        int size = keys.length;
        Node[] bucket = new Node[bucketSize];
        for (int i = 0; i < bucketSize; i++) {
            bucket[i] = new Node();
        }

        for (int i = 0; i < size; i++) {
            Node node = new Node();
            node.key = keys[i];
            int index = keys[i] / 10;
            Node p = bucket[index];
            if (p.key == 0) {
                bucket[index].next = node;
                bucket[index].key++;
            } else {
                // 插入排序，p.next是最小，依次连接
                while (p.next != null && p.next.key <= node.key) {
                    p = p.next;
                }
                node.next = p.next;
                p.next = node;
                bucket[index].key++;
            }
        }

        for (int i = 0; i < bucketSize; i++) {
            for (Node k = bucket[i].next; k != null; k = k.next) {
                System.out.print(k.key + " ");
            }
        }
    }

    static class Node {
        int key;// 当前桶中数据量
        Node next;
    }

    public static void main(String[] args) {
        int array[] = {49, 38, 65, 97, 13, 27, 11, 14, 16};
        bucketSort(array, 10);
    }
}

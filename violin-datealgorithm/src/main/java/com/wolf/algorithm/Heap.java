package com.wolf.algorithm;

import org.junit.Test;

/**
 * Description: 数据结构-堆
 * 树高与节点数N的关系为：H=log2(N+1)
 * 堆的操作(insert/removeRoot)耗时基本都在上升和下降操作的循环方法中，复制操作的次数为树的高度减一，
 * 去掉常数，堆操作(insert/removeRoot)的时间复杂度为O(logN)
 * <p>
 * 堆相比二叉搜索树是弱序的，即堆每个节点的两个子节点并无关系，只是比父节点大于/小于。所以对于树的遍历、
 * 查找是不可用的(O(logn))，但对快速移除最大节点和快速插入新节点是可以的。
 * <p>
 * 移除最大节点，后用最后节点填补(用最后节点是为保证一个完全二叉树)，之后调整合适位置。
 * 添加节点，放到最后，与父节点比较，若大则上升直到根节点。
 *
 * @author 李超
 * @date 2020/01/06
 */
public class Heap {

    private Node<Integer>[] array;
    private int maxSize = 31;
    private int currentSize = 0;

    public Heap() {
        array = new Node[maxSize];
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    // 前提：已经是堆。插入最后，开始上升
    public boolean insert(int key) {
        if (currentSize == maxSize) {
            return false;
        }
        Node newNode = new Node<>(key);
        array[currentSize] = newNode;
        trickleUp(currentSize++);
        return true;
    }

    // 上升直到父节点为0或者index小于父值
    public void trickleUp(int index) {
        Node<Integer> newNode = array[index];
        int parent = (index - 1) / 2;
        while (index > 0 && array[parent].getItem() < newNode.getItem()) {
            array[index] = array[parent];
            index = parent;

            parent = (parent - 1) / 2;
        }
        array[index] = newNode;
    }

    // 前提：已经是堆。删除根节点并调整推
    public Node removeRoot() {
        if (isEmpty()) {
            return null;
        }
        Node root = array[0];
        array[0] = array[--currentSize];
        trickleDown(0);
        return root;
    }

    // 前提：已经是堆。下降过程
    public void trickleDown(int index) {
        int oldIndex = index;
        Node<Integer> top = array[index];
        int largerChild;

        while (index < currentSize / 2) {     // 至少有一个子节点
            int leftChild = 2 * index + 1;
            int rightChild = leftChild + 1;

            // 右子节点存在且右边大
            if (rightChild < currentSize && array[leftChild].getItem() < array[rightChild].getItem()) {
                largerChild = rightChild;
            } else {
                largerChild = leftChild;
            }

            if (top.getItem() > array[largerChild].getItem()) {
                break;
            } else {
                array[index] = array[largerChild];
                index = largerChild;
            }
        }

        if (oldIndex != index) {
            array[index] = top;
        }
    }

    public void displayHeap() {
        System.out.printf("array: ");
        for (int j = 0; j < currentSize; j++) {
            if (array[j] != null) {
                System.out.printf(array[j].getItem() + " ");
            } else {
                System.out.printf("-- ");
            }
        }
        System.out.println();

        int nBlanks = 32;
        int itemsPerRow = 1;
        int column = 0;
        int j = 0;
        String dots = "..................................";
        System.out.println(dots + dots);

        while (currentSize > 0) {
            if (column == 0) {
                for (int k = 0; k < nBlanks; k++) {
                    System.out.print(" ");
                }
            }
            System.out.print(array[j].getItem());
            if (++j == currentSize) break;

            if (++column == itemsPerRow) {
                nBlanks /= 2;
                itemsPerRow *= 2;
                column = 0;
                System.out.println();
            } else {
                for (int k = 0; k < nBlanks * 2 - 2; k++) {
                    System.out.print(" ");
                }
            }
        }

        System.out.println("\n" + dots + dots);
    }

    @Test
    public void testInsert() {
        Heap theHeap = buildHeap();
        theHeap.displayHeap();
    }

    private Heap buildHeap() {
        Heap theHeap = new Heap();

        theHeap.insert(10);
        theHeap.insert(20);
        theHeap.insert(30);
        theHeap.insert(40);
        theHeap.insert(50);
        theHeap.insert(60);
        theHeap.insert(70);
        theHeap.insert(80);
        theHeap.insert(90);
        return theHeap;
    }

    @Test
    public void testRemove() {
        Heap theHeap = buildHeap();
        theHeap.removeRoot();

        theHeap.displayHeap();
    }
}

package com.wolf.algorithm.bigdata;

import java.util.Random;

/**
 * Description: 海量数据中查找出重复元素或者清除重复元素，使用位图实现。
 * 例如，已知某个文件内包含一些电话号码，每个号码为8位数字，统计不同号码的个数。
 * 8位整数可以表示的最大十进制数为99999999，如果每个数字对应于位图中的一个bit位，那么存储八位整数大约需要99Mbit，又因为1Byte=8bit，
 * 所以99Mbit折合成内存为99/8=12.375MB的内存，即可以只用12.375MB的内存表示所有8位数电话号码的内容。
 *
 * @author 李超
 * @date 2020/01/21
 */
public class Duplicate {

    static int mmin = 10000000;
    static int mmax = 99999999;
    static int N = (mmax - mmin + 1);
    static int BITS_PER_WORD = 32;

    static int wordOffset(int b) {// 单词所在偏移量，分组
        return b / BITS_PER_WORD;
    }

    static int bitOffset(int b) {// 单词转换到bit后，在int中的位
        return 1 << (b % BITS_PER_WORD);
    }

    static void setBit(int[] words, int n) {
        n -= mmin;// N内的随机数
        words[wordOffset(n)] |= bitOffset(n);
    }

    static void clearBit(int[] words, int n) {
        words[wordOffset(n)] &= ~bitOffset(n);
    }

    static boolean getBit(int[] words, int n) {
        int bit = words[wordOffset(n)] & bitOffset(n);
        return bit != 0;
    }

    private static void sort(int[] arr) {

        // 存放位图，每一位(通过算法)对应mmin到mmax范围内的一个数
        int[] words = new int[1 + N / BITS_PER_WORD];// 一共有1 + N / BITS_PER_WORD个分组，每个分钟内有32位
        int count = 0;

        for (int word : arr) {
            setBit(words, word);
        }

        for (int i = 0; i < words.length; i++) {
            if (getBit(words, i)) {
                System.out.print(i + mmin + "  ");// 再加回去
                count++;
            }
        }
        System.out.println();
        System.out.println("总个数为：" + count);
    }

    public static void main(String[] args) {

        int length = 100;
        int[] arr = new int[length];

        // 生成mmin到mmax范围内的100个随机数
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = r.nextInt(N);
            arr[i] += mmin;// 保证在范围内，不然生成0~n-1之间，那都落在第一个槽里了
            System.out.print(arr[i] + "  ");
        }
        System.out.println();

        sort(arr);
    }

}

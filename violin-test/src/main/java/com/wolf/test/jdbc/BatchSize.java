package com.wolf.test.jdbc;

/**
 * Description:手工计算批量操作
 * <br/> Created on 2017/10/17 11:09
 *
 * @author 李超
 * @since 1.0.0
 */
public class BatchSize {

    public static void main(String[] args) {
        int size = 101;
        int batchSize = 12;
        int batchTimes = computeBatchTimes(size, 12);
        for (int i = 0; i < batchTimes; i++) {
            int from = computeBatchFromIndex(size, batchSize, i);
            int to = computeBatchToIndex(size, batchSize, i);
            System.out.println("from:"+from+" to:"+to);
        }
    }

    public static int computeBatchTimes(int listSize, int batchSize) {
        int times = 0;
        if (listSize % batchSize > 0) {
            times = listSize / batchSize + 1;
        } else {
            times = listSize / batchSize;
        }
        return times;
    }

    public static int computeBatchFromIndex(int listSize, int batchSize, int i) {
        int from = i * batchSize;
        if (from > listSize) {
            from = listSize;
        }
        return from;
    }

    public static int computeBatchToIndex(int listSize, int batchSize, int i) {
        int to = (i + 1) * batchSize;
        if (to > listSize) {
            to = listSize;
        }
        return to;
    }


}

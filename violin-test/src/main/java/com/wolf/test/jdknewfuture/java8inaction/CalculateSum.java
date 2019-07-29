package com.wolf.test.jdknewfuture.java8inaction;

import java.util.concurrent.RecursiveTask;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/07/24
 */
public class CalculateSum extends RecursiveTask<Long> {

    private static final long THRESHOLD = 10_000;

    private long start;
    private long end;

    public CalculateSum(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long sum = 0;

        long length = end - start;
        if (length < THRESHOLD) {
            for (long i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            long middle = (start + end) / 2;
            //异步执行左边
            CalculateSum calculateSumLeft = new CalculateSum(start, middle);
            calculateSumLeft.fork();
            //同步执行右边，也可能再划分再拆开左边
            CalculateSum calculateSumRight = new CalculateSum(middle + 1, end);
            Long rightResult = calculateSumRight.compute();
            //等待左边线程完毕
            Long leftResult = calculateSumLeft.join();
            //合并
            sum = leftResult + rightResult;
        }
        return sum;
    }
}

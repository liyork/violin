package com.wolf.test.jdknewfuture.java8inaction;

import org.junit.Test;

import java.util.Spliterator;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Description:
 * 并行流建议：
 * 要注意避免修改共享变量。
 * 用测试检查并行流性能对比之前的串行提升多少，测量、测量、再测量
 * 避免拆装箱
 * limit/findFirst操作在并行流上慢，因为依赖顺序。findAny会好些。
 * 还要考虑流的操作流水线的总计算成本。设N是要处理的元素的总数， Q是一个元素通过流水线的大致处理成本，
 * 则N*Q就是这个对成本的一个粗略的定性估计。 Q值较高就意味着使用并行流时性能好的可能性比较大
 * 对于较小的数据量，选择并行流几乎从来都不是一个好的决定。并行处理少数几个元素的好处还抵不上并行化造成的额外开销
 * 要考虑流背后的数据结构是否易于分解。ArrayList的拆分效率比LinkedList高，因为不用遍历，LongStream.rangeClosed高。
 * 考虑终端操作中合并步骤的代价是大是小
 *
 * @author 李超
 * @date 2019/07/23
 */
public class ParallelStreamTest {

    static ForkJoinPool forkJoinPool = new ForkJoinPool();

    @Test
    public void testParallel() {

        int range = 10_000_000;

        ElapseTimeTest.measureElapse(() -> {
            long sum = 0;
            for (int i = 0; i <= range; i++) {
                sum += i;
            }
            return sum;
        });

        //并行版本：但是并未加速，因为拆装箱费时，iterate很难并行执行,因为本次需要依赖上次的执行结果即i
        ElapseTimeTest.measureElapse(() ->
                Stream.iterate(1L, i -> i + 1)
                        .limit(range)
                        .parallel()
                        .reduce(0L, Long::sum));

        //免去拆装箱，快一些
        ElapseTimeTest.measureElapse(() ->
                LongStream.rangeClosed(1, range)
                        .reduce(0L, Long::sum));

        //生成范围，便于并行化处理
        ElapseTimeTest.measureElapse(() ->
                LongStream.rangeClosed(1, range)
                        .parallel()
                        .reduce(0L, Long::sum));

        System.out.println("core:" + Runtime.getRuntime().availableProcessors());
        //一开始结果是：-2004260032，用的int，越界了
        ElapseTimeTest.measureElapse(() ->
                forkJoinPool.invoke(new CalculateSum(1L, range)));
    }

    @Test
    public void testSplit() {

        final String s = " aaaa bbbb  cccc dddd e e e e e e e e e e e e e";
        countWords(s);

        Stream<Character> stream = IntStream.range(0, s.length())
                .mapToObj(s::charAt);
        int count1 = countWords(stream);
        System.out.println("count1:" + count1);

        Stream<Character> stream2 = IntStream.range(0, s.length())
                .mapToObj(s::charAt);
        int count2 = countWords(stream2.parallel());//错误，因为stream底层不知道你依据什么切分，他只知道对每个字符流操作，从任意位置切分
        System.out.println("count2:" + count2);

        //指定切分器
        Spliterator<Character> spliterator = new WordCounterSpliterator(s);
        Stream<Character> stream3 = StreamSupport.stream(spliterator, true);
        int count3 = countWords(stream3.parallel());
        System.out.println("count3:" + count3);
    }

    private void countWords(String s) {
        int counter = 0;
        boolean lastSpace = true;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if (lastSpace) counter++;
                lastSpace = false;
            }
        }

        System.out.println(counter);
    }

    private int countWords(Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(
                new WordCounter(0, true),
                WordCounter::accumulate,
                WordCounter::combine
        );
        return wordCounter.getCount();
    }
}

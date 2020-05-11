package com.wolf.algorithm;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * <br/> Created on 2017/5/26 15:56
 *
 * @author 李超
 * @since 1.0.0
 */
public class DynamicBalance {

    @Test
    public void test() {
        Map<String, AtomicInteger> map = new LinkedHashMap<>();
        map.put("a", new AtomicInteger());
        map.put("b", new AtomicInteger());
        map.put("c", new AtomicInteger());
        map.put("d", new AtomicInteger());
        map.put("e", new AtomicInteger());

        for (int i = 0; i < 10; i++) {
            System.out.println(balance1(map));
        }
    }

    @Test
    public void test2() {
        List<String> ips = new ArrayList<String>();
        ips.add("111");
        ips.add("222");
        ips.add("333");
        ips.add("444");

        Map<String, Integer> blockCount = new HashMap<String, Integer>();
        blockCount.put("111", 8);
        blockCount.put("222", 5);
        blockCount.put("333", 3);
        blockCount.put("444", 1);

        int random = balance2(blockCount, ips);
        System.out.println(random);
    }

    /**
     * 从左到右选择一个最小负载的,如果第一个最小则赋值firstValue后，他就是最小的了。
     * 执行结果：abcdeabcdeabcde
     *
     * @param map
     * @return
     */
    private String balance1(Map<String, AtomicInteger> map) {

        long firstValue = Long.MAX_VALUE;
        Map.Entry<String, AtomicInteger> firstEntry = null;

        for (Map.Entry<String, AtomicInteger> entry : map.entrySet()) {
            long value = entry.getValue().get();
            if (value < firstValue) {
                firstValue = value;
                firstEntry = entry;
            }
        }
        firstEntry.getValue().incrementAndGet();
        return firstEntry.getKey();
    }

    /**
     * 动态负载，负载高的段位少，被随机的概率低
     *
     * @param blockCount ip 负载数
     * @param ips
     * @return
     */
    public static int balance2(Map<String, Integer> blockCount, List<String> ips) {

        int sumBlockCount = 0;
        int j = 0;
        List<Integer> blockCountList = new ArrayList<Integer>();
        List<Integer> subsectionList = new ArrayList<Integer>();

        for (String ip : ips) {
            Integer count = Integer.parseInt(blockCount.get(ip).toString());
            blockCountList.add(count);
            sumBlockCount = sumBlockCount + count;
        }

        //分段的开始下标，下个负载从下个分段开始
        int subsectionStartIndex = 0;
        for (Integer localCount : blockCountList) {
            //sumBlockCount - localCount，机器本身如果权重阻塞很小，则得到的分段越大
            subsectionStartIndex = subsectionStartIndex + (sumBlockCount - localCount);
            subsectionList.add(subsectionStartIndex);
        }

        Random rand = new Random();
        int randNum = rand.nextInt(subsectionStartIndex);
        //分段大的区间被随机的概率大
        for (int i = 0; i < subsectionList.size(); i++) {
            if (randNum < subsectionList.get(i)) {
                j = i;
                break;
            }
        }

        return j;
    }
}

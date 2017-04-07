package com.wolf.algorithm;

import java.util.*;

/**
 * Description:
 * <br/> Created on 2017/4/1 9:13
 *
 * @author 李超
 * @since 1.0.0
 */
public class RandomSubsection {

    public static void main(String[] args) {
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

        int random = random(blockCount, ips);
        System.out.println(random);
    }

    public static int random(Map<String, Integer> blockCount, List<String> ips) {
        int sumBlockCount = 0;
        int subsection = 0;
        int j = 0;
        List<Integer> blockCountList = new ArrayList<Integer>();
        List<Integer> subsectionList = new ArrayList<Integer>();

        for(String ip : ips) {
            Integer count = Integer.parseInt(blockCount.get(ip).toString());
            blockCountList.add(count);
            sumBlockCount = sumBlockCount + count;
        }

        for(Integer localCount : blockCountList) {
            //sumBlockCount - localCount，机器本身如果权重阻塞很小，则加总后得到的分段越大
            subsection = subsection + (sumBlockCount - localCount);
            subsectionList.add(subsection);
        }
        Random rand = new Random();
        int randNum = rand.nextInt(subsection);
        //分段大的区间被随机的概率大
        for(int i = 0; i < subsectionList.size(); i++) {
            if(randNum < subsectionList.get(i)) {
                j = i;
                break;
            }
        }

        return j;
    }
}

package com.wolf.test.base.map;

import org.junit.Test;

import java.util.*;

/**
 * Description:
 * hashmap约定：equals相同的hashcode要相同，因为要放在hashmap的相同槽中。
 * <br/> Created on 2017/6/23 13:35
 *
 * @author 李超
 * @since 1.0.0
 */
public class HashMapTest {

    public static void main(String[] args) {
//        baseTest();
        testResize();
//        testSetInitialSize();
//        testTreeifyBinInJdk8();
    }

    private static void baseTest() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("a", 2);//key重复，则值被覆盖
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        map.put("a1", 5);
        map.put("b2", 6);
        map.put("q1", 7);//如果key相等则将这个entity放到槽中，然后next指向以前的。例如：q1-->c

        Integer c = map.get("c");//for循环定位槽中的所有entity
        System.out.println(c);

        Set<String> keys = map.keySet();//简单构造KeySet
        Iterator<String> iterator = keys.iterator();//构造KeyIterator extends HashIterator，初始化一些变量next、index，用于后期遍历
        //过程：从数组第一个不为null的槽开始，一直到next为null，下个槽，直到最后一个槽
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }
        System.out.println();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    /**
     * 默认长度16，加载因子是0.75，阈值就是16*0.75=12，当size>阈值时resize为2倍
     * 容量一定是2的幂次方
     */
    private static void testResize() {
        HashMap<String, Integer> map = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int i1 = random.nextInt(100);
            map.put(i1 + "", i1);
        }
    }


    /**
     * 有人提议使用map时如果预知多少，直接设定初始大小，size/0.75+1，这个是由于map中的负载因子是0.75，大于等于这个size*0.75则resize，
     * 下面试了下，看了下源码，不对，源码中使用2的幂次方和给定值算出初始值，如果要放入15个数，一般的话写入15，那么他算出16，那么ok，
     * 如果使用15/0.75+1=21了。。。那么hashmap只能使用32.。
     * 结论是使用多大直接给入。
     */
    private static void testSetInitialSize() {
        int size = 15;
        int initialCapacity = (int) (size / 0.75) + 1;
        HashMap<String, Integer> map = new HashMap<String, Integer>(initialCapacity);

        for (int i = 0; i < size; i++) {
            map.put("i:" + i, i);
        }
    }

    // 测试jdk8hashmap的链表组织方式
    private static void testTreeifyBinInJdk8() {
        HashMap<Integer, List<String>> map2 = new HashMap<Integer, List<String>>();
        int h;
        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            int i2 = random.nextInt();
            String s = i2 + "";
            int i1 = (h = s.hashCode()) ^ (h >>> 16);
            //System.out.println("s:"+s+" i1:"+i1);
            List<String> strings = map2.get(i1);
            if (strings == null) {
                strings = new ArrayList<String>();
                map2.put(i1, strings);
            } else {
                strings.add(s);
            }
        }

        for (Map.Entry<Integer, List<String>> entry : map2.entrySet()) {
            List<String> value = entry.getValue();
            if (value.size() > 1) {
                System.out.println("key:" + entry.getKey() + " value:" + value);
            }
        }
    }

    //hashmap会根据key生成hashcode放入内部数组中，不能保证放入时的顺序
    @Test
    public void testHashMapOrder() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("a2", "a");
        map.put("a1", "b");
        map.put("a3", "b");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey());
        }
    }
}

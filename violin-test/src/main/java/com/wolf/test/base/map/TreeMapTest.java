package com.wolf.test.base.map;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * Description:
 * <br/> Created on 2018/6/15 11:49
 *
 * @author 李超
 * @since 1.0.0
 */
public class TreeMapTest {

    public static void main(String[] args) {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("3", 3);
        map.put("2", 2);
        map.put("5", 5);
        map.put("8", 8);
        map.put("7", 7);
        map.put("9", 9);

        Set<String> keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        //过程：红黑树存储，遍历使用中序遍历。先找到root，然后一直left到最小，然后打印，向上打印，向右向左一直下打印，向上打印
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }
    }
}

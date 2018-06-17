package com.wolf.test.base.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Description:
 * <br/> Created on 2018/6/15 11:42
 *
 * @author 李超
 * @since 1.0.0
 */
public class LinkedHashMapTest {

    public static void main(String[] args) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);

        Set<String> keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        //过程：内部维护了head，每次向后移动
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }
    }
}

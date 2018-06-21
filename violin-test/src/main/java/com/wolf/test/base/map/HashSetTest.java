package com.wolf.test.base.map;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Description:内部有HashMap<E,Object> map
 * value使用Object PRESENT = new Object()
 *
 * treeset类似，内部使用TreeMap
 * <br/> Created on 2018/6/21 9:58
 *
 * @author 李超
 * @since 1.0.0
 */
public class HashSetTest {

    public static void main(String[] args) {
        baseTest();
    }

    private static void baseTest() {
        HashSet<String> set = new HashSet<>();
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("d");
        set.add("a1");
        set.add("b2");
        set.add("q1");

        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {//同hashmap
            String next = iterator.next();
            System.out.println(next);
        }
    }
}

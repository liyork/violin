package com.wolf.test.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Description:
 * <br/> Created on 2017/6/23 13:35
 *
 * @author 李超
 * @since 1.0.0
 */
public class HashMapTest {

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        map.put("a1", 5);
        map.put("b2", 6);
        map.put("q1", 7);//如果key相等则将这个entity放到槽中，然后next指向以前的。例如：q1-->c

        Integer c = map.get("c");//for循环定位槽中的所有entity
        System.out.println(c);

        Set<String> keys = map.keySet();//简单构造KeySet
        Iterator<String> iterator = keys.iterator();//构造KeyIterator HashIterator，初始化一些变量next、index，用于后期遍历
        //过程：从数组第一个不为null的槽开始，遍历数组，遇到entity则next，如果为空则下个槽
        while(iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }
    }
}

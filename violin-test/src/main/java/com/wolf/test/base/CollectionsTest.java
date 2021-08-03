package com.wolf.test.base;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.apache.commons.collections.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Description:
 * <br/> Created on 2018/4/11 14:54
 *
 * @author 李超
 * @since 1.0.0
 */
public class CollectionsTest {

    public static void main(String[] args) {
        //testBase();
        //testString();
        testCompareTo("126", "62");
    }

    private static void testCompareTo(String s1, String s2) {
        System.out.println(s1.compareTo(s2));
    }

    private static void testBase() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(1);
        list.add(2);
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        System.out.println(list);
    }

    // 不对
    private static void testString() {
        ArrayList<String> list = new ArrayList<>();
        list.add("126");
        list.add("62");
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        System.out.println(list);
    }
}

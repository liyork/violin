package com.wolf.test.jvm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:编译后，泛型被擦除，使用强制转换
 * <br/> Created on 11/6/17 8:00 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class GenericErase {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("hell", "abc");
        map.put("xxx", "123");
        System.out.println(map.get("hell"));//(String)map.get("hell")
        System.out.println(map.get("xx"));
    }


    // ================================
    //both method have same erasure,由于泛型被擦除后两个方法一样，不能重载
//    public static void methodA(List<String> list) {
//        System.out.println("invoke methodA List<String>");
//    }

    public static void methodA(List<Integer> list) {
        System.out.println("invoke methodB List<Integer>");
    }


    // ================================

//    public static String methodB(List<String> list) {//通过不同返回值也不行
//        System.out.println("invoke methodA List<String>");
//        return "";
//    }

    public static int methodB(List<Integer> list) {
        System.out.println("invoke methodB List<Integer>");
        return 1;
    }

}

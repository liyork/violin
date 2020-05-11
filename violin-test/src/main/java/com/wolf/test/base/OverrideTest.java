package com.wolf.test.base;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Description:重载测试
 * <br/> Created on 2018/6/21 10:08
 *
 * @author 李超
 * @since 1.0.0
 */
public class OverrideTest {


    public void test(List<String> list) {

    }

//    编译报错：both methods have same erasure
//    public void test(List<Integer> list) {
//
//    }

    public void test(Object... list) {

    }

    //方法签名包含：方法名+参数，不包含返回值。所以这也算是相同方法。
//    public String test(List<String> list) {
//        return "11";
//    }

    public static void main(String[] args) throws ParseException {
        System.out.println(new Date(1581388576204l));
    }
}

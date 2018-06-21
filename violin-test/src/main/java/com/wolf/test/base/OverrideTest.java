package com.wolf.test.base;

import java.util.List;

/**
 * Description:
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
}

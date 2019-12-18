package com.wolf.test.base;

/**
 * Description:重写测试
 * <br/> Created on 2018/6/21 10:08
 *
 * @author 李超
 * @since 1.0.0
 */
public class OverwriteTest {

    class ParentA {
        public void test(String s) throws Exception {

        }
    }

    class SubA extends ParentA {
        //重写时必须一致，包括返回值
        //        @Override
//        public String test(String s) {
//            return "aa";
//        }

        //只允许抛出比父类若的异常类型，不能强于父类如Throwable
        @Override
        public void test(String s) throws RuntimeException {
        }
    }
}

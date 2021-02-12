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
        public Number test(String s) throws Exception {
            return 1;
        }
    }

    class SubA extends ParentA {
        //重写时必须一致，包括返回值
        //        @Override
//        public String test(String s) {
//            return "aa";
//        }

        //只允许抛出比父类弱的异常类型，不能强于父类，如Throwable
//        @Override
//        public Number test(String s) throws RuntimeException {
//            return 2;
//        }

        // 子类抛出异常必须等同父类的异常或子类异常
        @Override
        public Integer test(String s) throws RuntimeException {
            return 2;
        }
    }
}

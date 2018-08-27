package com.wolf.test.jdknewfuture;

/**
 * Description:函数式接口，单方法接口，可以使用lambda表达式
 *
 * 如果一个函数的参数接收一个单方法的接口而你传递的是一个function，则将行为传到函数里
 * <br/> Created on 2017/12/19 9:53
 *
 * @author 李超
 * @since 1.0.0
 */
@FunctionalInterface  //函数接口指的是只有一个函数的接口，这样的接口实现可以隐式转换为Lambda表达式  默认方法和静态方法不会破坏函数式接口的定义
interface Converter<F, T> {
    T convert(F from);

    //默认方法
    default void defaultMethod() {
        System.out.println("defaultMethod...");
    }

    //static methods
    static String create( Integer a ) {
        return a+"";
    }
}


package com.wolf.test.jdknewfuture.java8inaction;

/**
 * Description: 只定义[一个、抽象]方法的接口。
 * 哪怕有很多默认方法，只要接口只定义了一个抽象方法，它就仍然是一个函数式接口
 * <p>
 * 函数式接口中抽象方法的签名基本上就是Lambda表达式的签名。函数式接口的抽象方法的签名称为函数描述符。
 * () -> {}就是()->void。(String s) -> s.length()就是 String-> int
 *
 * 函数式接口，可以用于描述Lambda表达式的签名
 *
 * Function<T,R>  代表的函数描述:T->R
 * IntBinaryOperator  代表的函数描述:(int, int) -> int
 * Function<T,R>  代表的函数描述:T->R
 * Consumer<T>  代表的函数描述:T -> void
 * Supplier<T>/Callable<T>  代表的函数描述:()-> T
 * BiFunction<T, U, R>  代表的函数描述:(T,U) -> R
 *
 * @author 李超
 * @date 2019/07/19
 */
@FunctionalInterface
public interface FunctionInterfaceTest {

    void test();

    default void test1() {

    }
}

package com.wolf.test.jdknewfuture.java8inaction;

import com.wolf.utils.TimeUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/08/05
 */
public class FunctionCodeAction {

    //函数式编程一
    @Test
    public void testSubset() {

        List<Integer> integers = Arrays.asList(1, 4, 9);
        List<List<Integer>> subset = subset(integers);
        System.out.println(subset);
    }

    //方法：查询子集，不考虑顺序
    //列表第一个元素a与非第一个元素列表b
    //查询b的子集，用上述方式
    //为使用b的子集添加a元素得到c子集
    //合并c集合与b的子集
    private List<List<Integer>> subset(List<Integer> integers) {//函数式

        if (integers.isEmpty()) {
            List<List<Integer>> result = new ArrayList<>();
            result.add(Collections.emptyList());
            return result;
        }

        Integer first = integers.get(0);
        List<Integer> subList = integers.subList(1, integers.size());

        List<List<Integer>> subset = subset(subList);
        List<List<Integer>> addFirstSet = insertAll(first, subset);
        return concat(addFirstSet, subset);
    }

    private List<List<Integer>> insertAll(Integer first, List<List<Integer>> subset) {//函数式

        List<List<Integer>> result = new ArrayList<>();
        for (List<Integer> list : subset) {
            List<Integer> copy = new ArrayList<>();
            copy.add(first);
            copy.addAll(list);
            result.add(copy);
        }

        return result;
    }

    private List<List<Integer>> concat(List<List<Integer>> subset1, List<List<Integer>> subset2) {//函数式

        List<List<Integer>> result = new ArrayList<>(subset1);
        result.addAll(subset2);

        return result;
    }

    //函数式编程二
    @Test
    public void testFactorial() {

        System.out.println(testFactorialIterative(4));
        System.out.println(testFactorialRecursive(4));
        System.out.println(testFactorialStream(4));
        System.out.println(testFactorialTailRecursive(1, 4));
    }

    private int testFactorialIterative(int n) {

        int result = 1;
        for (int j = 1; j <= n; j++) {
            result *= j;
        }

        return result;
    }

    private int testFactorialRecursive(int n) {
        return n == 1 ? 1 : n * testFactorialRecursive(n - 1);
    }

    private int testFactorialStream(int n) {
        return IntStream.rangeClosed(1, n)
                .reduce(1, (a, b) -> a * b);
    }

    //tail-call optimization
    //基本的思想是你可以编写阶乘的一个迭代定义，不过迭代调用发生在函数的最后（所以我们说调用发生在尾部）。
    //递归调用发生在方法的最后,最后一个操作是乘以n，从而得到递归调用的结果
    //我们不需要在不同的栈帧上保存每次递归计算的中间值，编译器能够自行决定复用某个栈帧进行计算。
    //实际上，立即数（阶乘计算的中间结果）直接作为参数传递给了该方法。再也不用为每个递归调用分配单独
    //的栈帧用于跟踪每次递归调用的中间值——通过方法的参数能够直接访问这些值。编译器能够自行决定复用某个栈帧进行计算，只会分配一个栈帧
    //目前Java还不支持这种优化。Java不支持尾部调用消除（ tail call elimination），
    private int testFactorialTailRecursive(int acc, int n) {
        //为1时表明最后结果acc。否则持续*n带入下一步,n递减
        return n == 1 ? acc : testFactorialTailRecursive(acc * n, --n);
    }

    //函数式编程三
    @Test
    public void testCurried() {

        //一类相同转换因子和基线的功能，每次都传递三个参数繁琐。
        converter(4.0, 9.0, 3);
        converter(9.0, 9.0, 3);
        converter(2.0, 9.0, 3);

        //生产converter，同一类功能被抽取，而且复用底层通用方法
        DoubleUnaryOperator convertCtoF = curriedConverter(9.0, 3);
        DoubleUnaryOperator convertUSDtoGBP = curriedConverter(0.6, 0);
        //使用,有个此类converter调用就省事了。
        double gbp1 = convertCtoF.applyAsDouble(4.0);
        double gbp2 = convertCtoF.applyAsDouble(9.0);
        double gbp3 = convertCtoF.applyAsDouble(2.0);
    }

    //通用代码，过于宽泛。x是被转换的， f是转换因子， b是基线值
    double converter(double x, double f, double b) {
        return x * f + b;
    }

    //工厂方法，生产需要一个参数的转换方法
    static DoubleUnaryOperator curriedConverter(double f, double b) {
        return (double x) -> x * f + b;
    }

    //无法声明一个递归的Stream，因为Stream仅能使用一次。
    @Test
    public void testPrimes() {
        primes(IntStream.rangeClosed(1, 20)).forEach(System.out::println);
    }

    //递归方式查找质数
    //获取第一个元素，用剩余元素对第一个元素进行整除过滤掉非质数，
    //剩余元素同上操作
    static IntStream primes(IntStream numbers) {
        int head = head(numbers);
        return IntStream.concat(
                IntStream.of(head),
                primes(tail(numbers).filter(n -> n % head != 0))
        );
    }

    static IntStream numbers() {
        return IntStream.iterate(2, n -> n + 1);
    }

    static int head(IntStream numbers) {
        return numbers.findFirst().getAsInt();
    }

    static IntStream tail(IntStream numbers) {
        return numbers.skip(1);
    }

    @Test
    public void testLazyLinkedList() {

        //一般列表
        MyList<Integer> l = new MyLinkedList<>(5, new MyLinkedList<>(10, new Empty<>()));

        //延迟列表，延迟计算,使用时才计算
        MyList<Integer> numbers = from(2);
        int two = numbers.head();
        int three = numbers.tail().head();
        int four = numbers.tail().tail().head();
        System.out.println(two + " " + three + " " + four);
    }

    interface MyList<T> {
        T head();

        MyList<T> tail();

        default boolean isEmpty() {
            return true;
        }

        default MyList<T> filter1(Predicate<T> p) {
            return isEmpty() ?
                    this :
                    //head()当前元素的head
                    p.test(head()) ?
                            new LazyList<>(head(), () -> tail().filter1(p)) :
                            tail().filter1(p);
        }
    }

    class MyLinkedList<T> implements MyList<T> {
        private final T head;
        private final MyList<T> tail;

        public MyLinkedList(T head, MyList<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        public T head() {
            return head;
        }

        public MyList<T> tail() {
            return tail;
        }

        public boolean isEmpty() {
            return false;
        }


    }

    class Empty<T> implements MyList<T> {
        public T head() {
            throw new UnsupportedOperationException();
        }

        public MyList<T> tail() {
            throw new UnsupportedOperationException();
        }
    }

    static class LazyList<T> implements MyList<T> {
        final T head;
        final Supplier<MyList<T>> tail;//避免让tail立刻出现在内存中

        public LazyList(T head, Supplier<MyList<T>> tail) {
            this.head = head;

            this.tail = tail;
        }

        public T head() {
            return head;
        }

        public MyList<T> tail() {
            return tail.get();
        }

        public boolean isEmpty() {
            return false;
        }
    }

    //每次调用get，就会返回一个LazyList(n,() -> from(n + 1))的对象，自动延迟构造tail
    public static MyList<Integer> from(int n) {
        return new LazyList<>(n, () -> from(n + 1));
    }

    //延迟队列实现查找质数
    @Test
    public void testLazyPrimes() {

        MyList<Integer> numbers = from(2);
        MyList<Integer> primes = primes(numbers);
        //命令式编程打印
//        while (!primes.isEmpty()) {
//            System.out.println(primes.head());
//            primes = primes.tail();
//            TimeUtils.sleepMillisecond(500);
//        }
        //函数式编程打印
        printAll(primes);
    }

    public static MyList<Integer> primes(MyList<Integer> numbers) {
        Integer head = numbers.head();
        MyList<Integer> tail = numbers.tail();
        return new LazyList<>(head, () -> primes(tail.filter1(n -> n % head != 0)));
    }

    private void printAll(MyList<Integer> primes) {
        if (primes.isEmpty()) {
            return;
        }

        System.out.println(primes.head());
        TimeUtils.sleepMillisecond(500);
        printAll(primes.tail());
    }

    @Test
    public void testRepeat() {

        System.out.println(repeat(3, (Integer x) -> 2 * x).apply(3));
    }

    //x.compose(y)==>n->x.apply(y.apply(n))
    //f.apply(f.apply(f.apply()))
    private static <A> Function<A, A> repeat(int n, Function<A, A> f) {
        return n == 0 ? x -> x : f.compose(repeat(n - 1, f));
    }
}

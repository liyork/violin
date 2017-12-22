package com.wolf.test.jdkfuture;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Description:Lambda
 * <p>
 * 匿名类与lambda区别
 * <p>
 * 1.匿名类的 this 关键字指向匿名类，而lambda表达式的 this 关键字指向包围lambda表达式的类。测试不通过，应该指的使用局部变量
 * 2.编译方式。Java编译器将lambda表达式编译成类的私有方法。使用了Java 7的 invokedynamic 字节码指令来动态绑定这个方法。
 * <p>
 * Lambda表达式在Java中又称为闭包或匿名函数
 * <br/> Created on 2017/12/20 18:10
 *
 * @author 李超
 * @since 1.0.0
 */
public class LambdaTest {


    public static void main(String[] args) {

//        simpleTest();


//        simple2Test();


        //向API方法添加逻辑，用更少的代码支持更多的动态行为
        List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp", "Jsdf");

        //testFilter(languages);

        //合并
//        combine(languages);


        //map();

        mapReduce();

        //filter();

        //distinct();

        //statistics();


    }

    private static void statistics() {
        //获取数字的个数、最小值、最大值、总和以及平均值
        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x).summaryStatistics();
        System.out.println("Highest prime number in List : " + stats.getMax());
        System.out.println("Lowest prime number in List : " + stats.getMin());
        System.out.println("Sum of all prime numbers : " + stats.getSum());
        System.out.println("Average of all prime numbers : " + stats.getAverage());
    }

    private static void distinct() {
        List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
        //collect输出
        List<Integer> distinct = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
        System.out.printf("Original List : %s,  Square Without duplicates : %s %n", numbers, distinct);
    }

    private static void filter() {
        List<String> strList = Arrays.asList("abc", "bcd", "defg", "jk");
        // x -> x.length() > 2  结果为true则要
        List<String> filtered = strList.stream().filter(x -> x.length() > 2).collect(Collectors.toList());
        System.out.printf("Original List : %s, filtered list : %s %n", strList, filtered);
    }

    private static void mapReduce() {
        // 为每个订单加上12%的税
        // 老方法：
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        double total = 0;
        for (Integer cost : costBeforeTax) {
            double price = cost + .12 * cost;
            total = total + price;
        }
        System.out.println("Total : " + total);

        // 新方法：
        List<Integer> costBeforeTax2 = Arrays.asList(100, 200, 300, 400, 500);
        //sum + cost 与 sum = sum + cost一样
        double bill = costBeforeTax2.stream().map((cost) -> cost + .12 * cost).reduce((sum, cost) -> sum + cost).get();
        System.out.println("Total : " + bill);
    }

    //map将集合类（例如列表）元素进行转换的
    private static void map() {
        // 不使用lambda表达式为每个订单加上12%的税
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        for (Integer cost : costBeforeTax) {
            double price = cost + .12 * cost;
            System.out.println(price);
        }

        // 使用lambda表达式
        List<Integer> costBeforeTax2 = Arrays.asList(100, 200, 300, 400, 500);
        //将其应用到流中的每一个元素
        costBeforeTax2.stream().map((cost) -> cost + .12 * cost).forEach(System.out::println);

        // 将字符串换成大写并用逗号链接起来
        List<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany", "Italy", "U.K.", "Canada");
        String G7Countries = G7.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
        System.out.println(G7Countries);
    }

    private static void combine(List<String> languages) {
        Predicate<String> startsWithJ = (n) -> n.startsWith("J");
        Predicate<String> fourLetterLong = (n) -> n.length() == 4;
        languages.stream()
                .filter(startsWithJ.and(fourLetterLong))
                .forEach((n) -> System.out.print("nName, which starts with 'J' and four letter long is : " + n));
    }

    private static void testFilter(List<String> languages) {
        System.out.println("Languages which starts with J :");
        filter(languages, (str) -> str.startsWith("J"));//根据filter方法的参数是Predicate推断test

        System.out.println("Languages which ends with a ");
        filter(languages, (str) -> str.endsWith("a"));

        System.out.println("Print all languages :");
        filter(languages, (str) -> true);

        System.out.println("Print no language : ");
        filter(languages, (str) -> false);

        System.out.println("Print language whose length greater than 4:");
        filter(languages, (str) -> str.length() > 4);
    }

    private static void simple2Test() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("runnable 1...");
            }
        }).start();
        //lambda
        new Thread(() -> System.out.println("xxxx")).start();

    }

    public static void filter(List<String> names, Predicate<String> condition) {
        for (String name : names) {
            if (condition.test(name)) {
                System.out.println(name + " ");
            }
        }
    }

    private static void simpleTest() {
        Arrays.asList("a", "b", "d").forEach(e -> System.out.println(e));//参数e，方法System.out.println( e )

        //lambda表达式内可以使用方法引用，仅当该方法不修改lambda表达式提供的参数,这仅是一个参数相同的简单方法调用
        Arrays.asList("a", "b", "d").forEach(System.out::println);//简化,方法引用由::双冒号操作符标示

        //多行要用大括号
        Arrays.asList("a", "b", "d").forEach(e -> {
            System.out.print(e);
            System.out.print(e);
        });

        //返回值
        Arrays.asList("a", "b", "d").sort((e1, e2) -> {
            return e1.compareTo(e2);
        });

        Arrays.asList("a", "b", "d").sort((e1, e2) -> e1.compareTo(e2));

        Arrays.asList("a", "b", "d").sort(String::compareTo);


        //构造实例,可见lambda表达式仅仅类似于匿名对象
        Converter<String, Integer> converter = (String from) -> {
            System.out.println(from);
            return Integer.valueOf(from);
        };
        Integer converted = converter.convert("123");
        System.out.println(converted);    // 123

        Converter<String, Integer> converter2 = (from) -> Integer.valueOf(from);

        Converter<String, Integer> converter3 = Integer::valueOf;//通过静态方法引用
        Integer converted2 = converter3.convert("123");
        System.out.println(converted2);   // 123

        //lambda内部可以使用静态、非静态和局部变量
        //局部变量,final也可以不声明，但是不能修改
        final int num = 1;
        Converter<Integer, String> stringConverter =
                (from) -> String.valueOf(from + num);
        String convert = stringConverter.convert(123);
        System.out.println("stringConverter==>" + convert);
//        num=2;错误

        //老式调用
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
        names.sort(new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return b.compareTo(a);
            }
        });
        System.out.println(names);
        //简化1
        names.sort((String a, String b) -> {
            return b.compareTo(a);
        });
        //简化2，一行省略大括号
        names.sort((String a, String b) -> b.compareTo(a));
        //简化3，自动推断类型
        names.sort((a, b) -> b.compareTo(a));


        //构造函数引用
        //Person::new获取Person类构造函数的引用,Java编译器会自动根据PersonFactory.create方法的签名来选择合适的构造函数。
        PersonFactory<Person> personFactory = Person::new;
        Person person = personFactory.create("Peter", "Parker");

        Predicate<String> predicate = (s) -> s.length() > 0;//根据test方法定位到Predicate
        predicate.test("foo");              // true
        predicate.negate().test("foo");     // false
        Predicate<String> nonNull = Objects::nonNull;
        System.out.println("nonNull==>" + nonNull.test(null));
        Predicate<String> isNull = Objects::isNull;
        System.out.println("isNull==>" + isNull.test(null));
        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();
        System.out.println("isNotEmpty==>" + isNotEmpty.test(""));


        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        String apply = backToString.apply("123");
        System.out.println("apply==>" + apply);


        List<String> stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");

        stringCollection
                .stream()
                .filter((s) -> s.startsWith("a"))
                .forEach(System.out::println);

    }
}

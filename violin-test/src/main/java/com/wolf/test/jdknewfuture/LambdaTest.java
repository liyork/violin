package com.wolf.test.jdknewfuture;

import com.wolf.utils.ArrayUtils;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Description:Lambda
 * The high-level goal of Project Lambda is to enable programming patterns that require modeling code as data to
 * be convenient and idiomatic in Java.
 * 随着回调模式和函数式编程风格的日益流行，我们需要在Java中提供一种尽可能轻量级的将代码封装为数据（Model code as data）的方法
 *
 * <p>
 * Lambda表达式在Java中又称为闭包或匿名函数。用匿名类时，定义行为最重要的那行代码，却混在中间不够突出
 * <p>
 * (parameters) -> expression or (parameters) -> {statements;}
 * (parameters)表示signature     ->表示lambda operator     expression表示method implementation
 * <p>
 * lambda表达式是一段可以传递的代码，它的核心思想是将面向对象中的传递数据变成传递行为。代码即数据
 * 使用->将参数和实现逻辑分离，当运行这个线程的时候执行的是->之后的代码片段，且编译器帮助我们做了类型推导
 * <p>
 * lambda不仅仅是功能，而是创建了一个函数(对象)。不过真正用时还要执行调用。
 * <p>
 * 作为开发人员，我发现学习和掌握lambda表达式的最佳方法就是勇于尝试，尽可能多练习lambda表达式例子
 * <p>
 * javap -c -v XXXX(class位置)
 * <p>
 * <br/> Created on 2017/12/20 18:10
 *
 * @author 李超
 * @since 1.0.0
 */
public class LambdaTest {


    public static void main(String[] args) throws Exception {

//        show();

//        testEvolutionConverter();
//        testEvolutionRunnable();
//        testEvolutionForeach();
//        testEvolutionCompareSort();
//        testDefault();

//        testMethodRef();

//          testScope();

//        testPreinstallFuncUse();

//        testStreamRelativeFunc();
//        testMapApp();
//        testMapReduceApp();
//        testCombineApp();
//        testDistinctApp();
//        testParallelStream();
//        testLazy();

//        statistics();
//        new LambdaTest().testDiffAnonymousClass();

//        testFlagMap();
//        testOptional();
//        testGroupby();
        testMerge();
    }

    private static void show() {
//        () -> 10;
//
//        (int x, int y) -> x + y;
//
//        (x, y) -> x + y;
//
//        name -> System.out.println(name);
//
//        (String name,String sex) -> {System.out.println(name);System.out.println(sex);}
//
//        x -> 2 * x;
    }


    private static void testEvolutionConverter() {

        String convertValue = "123123";
        //以前使用方式
        Converter<String, Integer> oldConverter = new Converter<String, Integer>() {
            @Override
            public Integer convert(String from) {
                return Integer.parseInt(from);
            }
        };
        Integer oldConvert = oldConverter.convert(convertValue);
        System.out.println(oldConvert);


        //lambda表达式类似于匿名对象
        Converter<String, Integer> newConverter = (String from) -> {
            System.out.println(from);
            return Integer.valueOf(from);
        };
        Integer newConvert = newConverter.convert(convertValue);
        System.out.println(newConvert);

        //再进一步，自动判断方法参数类型。单方法去掉{}
        Converter<String, Integer> converter2 = (from) -> Integer.valueOf(from);

        //再进一步，参数自动判断。静态方法调用::
        Converter<String, Integer> converter3 = Integer::valueOf;
        Integer converted2 = converter3.convert(convertValue);
        System.out.println(converted2);

    }

    private static void testEvolutionRunnable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("11111");
            }
        }).start();

        new Thread(() -> System.out.println("2222")).start();
    }

    private static void testEvolutionForeach() {
        int[] array = {4, 5, 6};
        Arrays.stream(array)//流对象
                .forEach(new IntConsumer() {
                    @Override
                    public void accept(int value) {
                        System.out.println(value);
                    }
                });

        //基本lambda
        Arrays.stream(array).forEach((int x) -> {
            System.out.println(x);
        });

        //参数类型可以从上下文推导出
        Arrays.stream(array).forEach((x) -> {
            System.out.println(x);
        });

        //当方法去掉括号
        Arrays.stream(array).forEach((x) -> System.out.println(x));

        //实例方法引用
        Arrays.stream(array).forEach(System.out::println);

        //有返回值
        ArrayUtils.toList("a", "b", "d").sort((e1, e2) -> {
            return e1.compareTo(e2);
        });

        ArrayUtils.toList("a", "b", "d").sort((e1, e2) -> e1.compareTo(e2));

        ArrayUtils.toList("a", "b", "d").sort(String::compareTo);
    }

    private static void testEvolutionCompareSort() {
        //老式调用
        List<String> names = ArrayUtils.toList("peter", "anna", "mike", "xenia");
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

        //实例方法调用
        names.sort(Comparator.reverseOrder());
    }

    private static void testDefault() {
        Converter<Integer, String> converter = (a) -> {
            System.out.println("22222");
            return "111";
        };

        String convert = converter.convert(111);
        System.out.println(convert);

        converter.defaultMethod();

        //Iterable中的default的forEach
        List<Integer> integers = ArrayUtils.toList(1, 4, 5, 8, 10);
        integers.forEach(System.out::println);
    }

    private static void testPreinstallFuncUse() {

        //Predicate用来逻辑判断
        Predicate<String> predicate = (s) -> s.length() > 0;//根据test方法的入参和出参定位到Predicate函数
        //对每个参数使用s.length() > 0 表达式验证
        predicate.test("foo");
        predicate.negate().test("foo");

        //对参数使用Objects::nonNull测试
        Predicate<String> nonNull = Objects::nonNull;
        System.out.println("nonNull==>" + nonNull.test(null));

        Predicate<String> isNull = Objects::isNull;
        System.out.println("isNull==>" + isNull.test(null));

        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();
        System.out.println("isNotEmpty==>" + isNotEmpty.test(""));


        //有输入有输出
        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        String apply = backToString.apply("123");
        System.out.println("apply==>" + apply);


        //无输入，有输出
        Supplier<String> supplier = () -> "test Supplier";
        System.out.println(supplier.get());

        //有输入，无输出
        Consumer<Integer> consumer = (a) -> {
            int b = a * 2;
            System.out.println("consumer:" + b);
        };
        consumer.accept(44);

    }


    //方法引用是Lambda表达式的一个简化写法。lambda表达式内的方法可以使用方法引用。所引用的方法，其实是那个Lambda表达式内方法体的实现
    //仅当该方法不修改lambda表达式提供的参数
    //方法引用,lambda内似乎只有一个调用其他对象的方法，就可以这么干？
    private static void testMethodRef() {

        //静态方法引用
        Converter<Integer, String> stringConverter = (from) -> Converter.create(from);
        String convert = stringConverter.convert(1111);
        System.out.println(convert);

        Converter<Integer, String> stringConverter1 = Converter::create;


        //实例方法引用,省去参数申明和传递
        Converter<String, Integer> stringConverter2 = (from) -> new Helper().string2Int(from);

        Helper helper = new Helper();
        Converter<String, Integer> stringConverter3 = helper::string2Int;

        int[] array = {4, 5, 6};
        Arrays.stream(array).forEach(System.out::println);

        IntConsumer outConsumer = System.out::println;
        IntConsumer errConsumer = System.err::println;
        Arrays.stream(array).forEach(outConsumer.andThen(errConsumer));


        //构造函数引用
        PersonFactory<Person> personFactory = new PersonFactory() {
            @Override
            public Person create(String firstName, String lastName) {
                return new Person(firstName, lastName);
            }
        };
        personFactory.create("a", "b");

        PersonFactory<Person> pF = Person::new;
        pF.create("a1", "b1");//Java编译器会自动根据PersonFactory.create方法的签名来选择合适的构造函数。

    }

    private static void testScope() {

        //lambda内部可以使用静态、非静态和局部变量
        //局部变量,final也可以不声明，但是不能修改
        final int num = 1;
        Converter<Integer, String> stringConverter = (from) -> String.valueOf(from + num);
        String convert = stringConverter.convert(1212);
        System.out.println("stringConverter==>" + convert);
    }

    private static void statistics() {
        //获取数字的个数、最小值、最大值、总和以及平均值
        String[] strings = {"2", "3", "5", "7"};
        List<String> primes = ArrayUtils.toList(strings);
        IntSummaryStatistics stats = primes.stream().mapToInt(Integer::parseInt).summaryStatistics();
        System.out.println("Highest prime number in List : " + stats.getMax());
        System.out.println("Lowest prime number in List : " + stats.getMin());
        System.out.println("Sum of all prime numbers : " + stats.getSum());
        System.out.println("Average of all prime numbers : " + stats.getAverage());
    }

    //匿名类与lambda区别
    //1.匿名类的 this 关键字指向匿名类，而lambda表达式的 this 关键字指向包围lambda表达式的类
    //2.编译方式。Lambda表达式本质上是匿名方法，Java编译器将lambda表达式编译成类的私有方法。使用了Java 7的 invokedynamic 字节码指令来动态绑定这个方法。
    private void testDiffAnonymousClass() {

        Converter<String, Integer> converter = new Converter<String, Integer>() {
            @Override
            public Integer convert(String from) {
                System.out.println("this:" + this);
                return null;
            }
        };
        converter.convert("1111");

        Converter<String, Integer> converter2 = (a) -> {
            System.out.println("lambada this:" + this);
            return null;
        };
        converter2.convert("22222");
    }


    //java8中支持对集合对象的stream对象进行函数式操作
    //Stream表示数据流，它没有数据结构，本身也不存储元素，其操作也不会改变源Stream，而是生成新Stream
    //中间操作(形成管道)/完结操作,尽可能以“延迟”的方式运行
    //Stream既支持串行也支持并行
    //stream操作步骤：创建——>变化——>完结
    private static void testStreamRelativeFunc() {
        List<String> languages = ArrayUtils.toList("Java", "Scala", "C++", "Haskell", "Lisp", "Jsdf");

        System.out.println("Languages which starts with J :");
        filter(languages, (str) -> str.startsWith("J"));//根据filter方法的参数是Predicate推断test

        System.out.println("Print all languages :");
        filter(languages, (str) -> true);

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
                .filter((s) -> s.equals("aaa1"))
                .forEach(System.out::println);

        //sorted
        stringCollection
                .stream()
                .sorted((o1, o2) -> o1.compareTo(o2))
                .forEach((s) -> System.out.println("sorted:" + s));

        //map，映射
        stringCollection
                .stream()
                .map((s) -> s + "__xx")
                .forEach((s) -> System.out.println("map:" + s));

        //match
        boolean isMatch = stringCollection
                .stream()
                .anyMatch((s) -> s.startsWith("a"));
        System.out.println(isMatch);

        //collect
        List<String> collect = stringCollection
                .stream()
                .map((s) -> s + "__yy")
                .collect(Collectors.toList());
        System.out.println(collect);

        //count
        long count = stringCollection
                .stream()
                .filter((s) -> s.startsWith("a"))
                .count();
        System.out.println(count);

        //reduce，操作后的值作为下次操作的第一个值
        Optional<String> reduce = stringCollection
                .stream()
                .reduce((s1, s2) -> {
                    System.out.println("s1:" + s1 + ",s2:" + s2);
                    return s1 + "|" + s2;
                });
        System.out.println(reduce);
    }

    private static void filter(List<String> names, Predicate<String> condition) {
        //老方式
        for (String name : names) {
            if (condition.test(name)) {
                System.out.println(name + " ");
            }
        }

        //新方式
        names.stream().filter(condition).forEach(System.out::println);
    }


    //map将集合类（例如列表）元素进行转换的
    private static void testMapApp() {
        // 不使用lambda表达式为每个订单加上12%的税
        Integer[] arr = {100, 200, 300, 400, 500};
        ArrayList<Integer> costBeforeTax = ArrayUtils.toList(arr);
        for (Integer cost : costBeforeTax) {
            double price = cost + .12 * cost;
            System.out.println(price);
        }

        // 使用lambda表达式
        Integer[] arr1 = {100, 200, 300, 400, 500};
        List<Integer> costBeforeTax2 = ArrayUtils.toList(arr1);
        //将其应用到流中的每一个元素
        costBeforeTax2.stream().map((cost) -> cost + .12 * cost).forEach(System.out::println);

        // 将字符串换成大写并用逗号链接起来
        List<String> G7 = ArrayUtils.toList("USA", "Japan", "France", "Germany", "Italy", "U.K.", "Canada");
        String G7Countries = G7.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
        System.out.println(G7Countries);
    }

    private static void testMapReduceApp() {
        // 为每个订单加上12%的税
        // 老方法：
        List<Integer> costBeforeTax = ArrayUtils.toList(100, 200, 300, 400, 500);
        double total = 0;
        for (Integer cost : costBeforeTax) {
            double price = cost + .12 * cost;
            total = total + price;
        }
        System.out.println("Total : " + total);

        // 新方法：
        List<Integer> costBeforeTax2 = ArrayUtils.toList(100, 200, 300, 400, 500);
        //sum + cost 与 sum = sum + cost一样
        double bill = costBeforeTax2.stream().map((cost) -> cost + .12 * cost).reduce((sum, cost) -> sum + cost).get();
        double bill2 = costBeforeTax2.stream().map((cost) -> cost + .12 * cost).reduce((sum, cost) -> {
            System.out.println("sum:" + sum + ",cost:" + cost);
            return sum + cost;
        }).get();
        System.out.println("Total : " + bill + ",bill2:" + bill2);

    }

    private static void testCombineApp() {

        List<String> languages = ArrayUtils.toList("Java", "Scala", "C++", "Haskell", "Lisp", "Jsdf");

        Predicate<String> startsWithJ = (n) -> n.startsWith("J");
        Predicate<String> fourLetterLong = (n) -> n.length() == 4;
        languages.stream()
                .filter(startsWithJ.and(fourLetterLong))
                .forEach((n) -> System.out.print("nName, which starts with 'J' and four letter long is : " + n));
    }

    private static void testDistinctApp() {
        List<Integer> numbers = ArrayUtils.toList(9, 10, 3, 4, 7, 3, 4);
        //collect输出
        List<Integer> distinct = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
        System.out.printf("Original List : %s,  Square Without duplicates : %s %n", numbers, distinct);
    }

    private static void testParallelStream() {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            list.add(UUID.randomUUID().toString());
        }

        long start = System.nanoTime();
        String collect = list.stream().sorted().collect(Collectors.joining(","));
        long end = System.nanoTime();
        long millis = TimeUnit.NANOSECONDS.toMillis(end - start);
        System.out.println("serial stream cost:" + millis);

        Collections.reverse(list);

        long start1 = System.nanoTime();
        String collect1 = list.parallelStream().sorted().collect(Collectors.joining(","));
        long end1 = System.nanoTime();
        long millis1 = TimeUnit.NANOSECONDS.toMillis(end1 - start1);
        System.out.println("serial stream cost:" + millis1);

        System.out.println("equals:" + collect.equals(collect1));
    }

    static long value = 0;

    private static void testLazy() throws InterruptedException {

        Stream<Long> longStream = Stream.generate(() -> {
            System.out.println("value:" + value);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return value++;
        }).map((p) -> p + 1);

        longStream.limit(1000).count();//注释掉这行，则上面的map不执行，因为懒加载。要遇到完结方法才开始执行。

        System.out.println("1111");
        Thread.sleep(111111);
    }

    @Test
    public void testConcurrent() {
        //并行流
        long count = IntStream.range(1, 100000).filter(PrimeUtil::isPrime).count();//串行
        System.out.println(count);
        long count2 = IntStream.range(1, 100000).parallel()//并行流
                .filter(PrimeUtil::isPrime)//被多线程并发调用
                .count();
        System.out.println(count2);

        //集合转并行流
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        double asDouble = list.stream().mapToInt(s -> s).average().getAsDouble();
        System.out.println(asDouble);
        double asDouble2 = list.parallelStream().mapToInt(s -> s).average().getAsDouble();
        System.out.println(asDouble2);

        //并行排序
        int[] arr = {8, 4, 3, 7, 1, 2, 9, 5};
        Arrays.parallelSort(arr);
        System.out.println(Arrays.toString(arr));

        //串行随机赋值
        Random random = new Random();
        Arrays.setAll(arr, (i) -> random.nextInt());
        System.out.println(Arrays.toString(arr));
        //并行随机赋值
        Arrays.parallelSetAll(arr, (i) -> random.nextInt());
        System.out.println(Arrays.toString(arr));

    }

    @Test
    public void testCompletableFuture() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        new Thread(new AskThread(future)).start();
        Thread.sleep(4000);
        future.complete(60);//手动设定值，完成状态


        //异步调用，回调通知
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> call(50));
        System.out.println(completableFuture.get());
        //异步调用无返回值
        CompletableFuture.runAsync(() -> call(50));

        //流式调用
        CompletableFuture<Void> fu = CompletableFuture.supplyAsync(() -> call(50))
                .thenApply((i) -> Integer.toString(i))
                .thenApply((str) -> "\"" + str + "\"")
                .thenAccept(System.out::println);
        fu.get();//等待call完成

        //异常处理
        CompletableFuture<Void> fu2 = CompletableFuture.supplyAsync(() -> callException(50))
                .exceptionally(ex -> {
                    System.out.println(ex.toString());
                    return 0;
                })
                .thenApply((i) -> Integer.toString(i))
                .thenApply((str) -> "\"" + str + "\"")
                .thenAccept(System.out::println);
        fu2.get();

        //组合
        CompletableFuture<Void> fu3 = CompletableFuture.supplyAsync(() -> call(50))
                .thenCompose((i) -> CompletableFuture.supplyAsync(() -> call(i)))
                .thenApply((str) -> "\"" + str + "\"")
                .thenAccept(System.out::println);
        fu3.get();//等待call完成

        //组合2
        CompletableFuture<Integer> intFuture = CompletableFuture.supplyAsync(() -> call(5));
        CompletableFuture<Integer> intFuture2 = CompletableFuture.supplyAsync(() -> call(7));
        CompletableFuture<Void> fu4 = intFuture.thenCombine(intFuture2, (i, j) -> (i + j))
                .thenApply((str) -> "\"" + str + "\"")
                .thenAccept(System.out::println);
        fu4.get();

    }

    //forEach中的return类似于continue
    @Test
    public void testReturn() {

        IntStream.range(0, 3).forEach(i -> {
            if (i == 1) {
                return;
            }

            System.out.println("xxxx:" + i);
        });
    }

    static class AskThread implements Runnable {
        CompletableFuture<Integer> re = null;

        public AskThread(CompletableFuture<Integer> re) {
            this.re = re;
        }

        @Override
        public void run() {
            int myRe = 0;
            try {
                myRe = re.get() * re.get();//若未完成阻塞
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println(myRe);
        }
    }

    private static Integer call(Integer para) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return para * para;
    }

    private static Integer callException(Integer para) {

        return para / 0;
    }

    private static void testFlagMap() {

        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);

        //map操作stream中每一个
        integers.stream().map(i -> i + 1).forEach(System.out::println);

        System.out.println("xxxxxx");

        List<List<Integer>> outer = new ArrayList<>();
        List<Integer> inner1 = new ArrayList<>();
        inner1.add(1);
        List<Integer> inner2 = new ArrayList<>();
        inner1.add(2);
        List<Integer> inner3 = new ArrayList<>();
        inner1.add(3);
        List<Integer> inner4 = new ArrayList<>();
        inner1.add(4);
        List<Integer> inner5 = new ArrayList<>();
        inner1.add(5);
        outer.add(inner1);
        outer.add(inner2);
        outer.add(inner3);
        outer.add(inner4);
        outer.add(inner5);
        List<Integer> result = outer.stream()
                //对stream中每个元素再stream+map，对每个元素放入list
                .flatMap(inner -> inner.stream().map(i -> i + 1))
                .collect(Collectors.toList());
        System.out.println(result);
    }

    private static void testOptional() {

        //不允许为空
        Optional<Integer> optionalInteger = Optional.of(111);
        Integer integer = optionalInteger.map(i -> i + 1).orElse(null);
        System.out.println(integer);

        String a = null;
        String s = Optional.ofNullable(a).map(Object::toString).orElse(null);
        System.out.println(s);
    }

    private static void testGroupby() {

        List<Person> persons = new ArrayList<>();
        persons.add(new Person("a", "1"));
        persons.add(new Person("b", "1"));
        persons.add(new Person("c", "2"));

        Map<String, List<Person>> collect = persons.stream()
                .collect(Collectors.groupingBy(Person::getLastName));

        for (Map.Entry<String, List<Person>> entry : collect.entrySet()) {
            String key = entry.getKey();
            List<Person> values = entry.getValue();
            System.out.println("key:" + key + " values:" + values);
        }
    }

    private static void testMerge() {

        Map<String, Integer> collect = new HashMap<>();
        collect.put("a", 1);

        Integer a = collect.merge("a", 2, (o, n) -> o + n);
        System.out.println("new a:" + a);
        System.out.println("new a:" + collect.get("a"));

        Map<String, AtomicInteger> collect1 = new HashMap<>();
        collect1.put("a", new AtomicInteger(1));

        AtomicInteger newAtomic = collect1.merge("a", new AtomicInteger(2),
                (o, n) -> {
                    o.addAndGet(n.get());
                    return o;
                });
        System.out.println("new a1:" + newAtomic.get());
        System.out.println("new a1:" + collect1.get("a").get());
    }

    //经过filter之后是空，对map无影响，不报错
    @Test
    public void testFilterNull() {

        List<Integer> list = Arrays.asList(1, 2, 3).stream()
                .filter(i -> i > 3)
                .map(i -> i + 1)
                .collect(Collectors.toList());

        System.out.println(list);

    }
}

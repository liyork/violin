package com.wolf.test.jdknewfuture.java8inaction;

import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Description: stream
 * 从支持数据处理操作的源生成的元素序列，元素序列、源、数据处理操作、流水线、内部迭代
 * 代码是以声明性方式写的：说明想要完成什么而不是说明如何实现一个操作
 * 把几个基础操作链接起来，来表达复杂的数据处理流水线
 * filter、 sorted、 map和collect等操作是与具体线程模型无关的高层次构件
 *
 * @author 李超
 * @date 2019/07/20
 */
public class StreamTest {

    @Test
    public void testNull() {
        List<String> list = new ArrayList<>();
        Stream<String> stream = list.stream();
//        stream.map((a) -> a.length());

        String s = "s";
        Stream<String> s1 = Stream.of(s);
        Stream<String> concat = Stream.concat(stream, s1);
        System.out.println();
    }

    @Test
    public void testCompare() {
        List<LbBean> list = new ArrayList<>();
        //筛选、排序、收集---之前写法
        List<LbBean> largeThanList = new ArrayList<>();
        for (LbBean lbBean : list) {
            if (lbBean.getMoney() < 400) {
                largeThanList.add(lbBean);
            }
        }

        largeThanList.sort(new Comparator<LbBean>() {
            @Override
            public int compare(LbBean o1, LbBean o2) {
                return o1.getMoney() - o2.getMoney();
            }
        });

        List<String> result = new ArrayList<>();
        for (LbBean lbBean : largeThanList) {
            result.add(lbBean.getName());
        }

        //筛选、排序、收集---stream
        list.stream()
                .filter(lb -> lb.getMoney() < 400)
                .sorted((a, b) -> a.getMoney() - b.getMoney())
                .map(lb -> lb.getName())
                .collect(Collectors.toList());

        list.stream()
                .filter(lb -> lb.getMoney() < 400)
                .sorted(Comparator.comparingInt(LbBean::getMoney))
                .map(LbBean::getName)
                .collect(Collectors.toList());

        list.parallelStream()//并行
                .filter(lb -> lb.getMoney() < 400)
                .sorted(Comparator.comparingInt(LbBean::getMoney))
                .map(LbBean::getName)
                .collect(Collectors.toList());
    }

    @Test
    public void test1() {

        Stream<Dish> stream = Data.menu.stream();
        List<String> collect = stream
                .filter(d -> d.getCalories() > 300)
                .map(Dish::getName)
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(collect);

        //流生成后只能消费一次
//        stream.forEach(System.out::println);
    }

    //简单日志查看执行方式。
    //limit操作和一种称为短路的技巧
    //尽管filter和map是两个独立的操作，但它们合并到同一次遍历中了
    @Test
    public void test2() {

        Stream<Dish> stream = Data.menu.stream();
        List<String> collect = stream
                .filter((Dish d) -> {
                    System.out.println("filter:" + d.getName());
                    return d.getCalories() > 300;
                })
                .map((Dish d) -> {
                    System.out.println("map:" + d.getName());
                    return d.getName();
                })
                .limit(3)
                .collect(Collectors.toList());

        System.out.println(collect);
    }

    //常用方法，针对流的每一个元素应用lambda参数
    @Test
    public void testCommonUse1() {
        System.out.println("distinct..");
        List<Integer> integers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        integers.stream()
                .filter((s) -> s % 2 == 0)
                .distinct()
                .forEach(System.out::println);

        System.out.println("filter+limit..");
        //filter和limit的组合,该方法只选出了符合谓词的头三个元素
        Data.menu.stream()
                .filter((s) -> s.getCalories() > 300)
                .limit(3)
                .forEach(s -> System.out.println(s.getName()));

        System.out.println("skip..");
        Data.menu.stream()
                .filter((s) -> s.getCalories() > 300)
                .skip(2)
                .forEach(s -> System.out.println(s.getName()));
    }

    @Test
    public void testCommonUse2() {

        System.out.println("map..");
        Data.menu.stream()
                .map(Dish::getName)
                .forEach(System.out::println);

        System.out.println("error map..");
        List<String> strings = Arrays.asList("hello", "world");
        strings.stream()
                .map(s -> s.split(""))//转换后流中是数组
                .distinct()
                .forEach(s -> System.out.println(Arrays.toString(s)));

        System.out.println("Arrays.stream..");
        List<String> strings1 = Arrays.asList("hello", "world");
        strings1.stream()
                .map(s -> s.split(""))
                .map(Arrays::stream)//转换后流中是Stream<String>
                .distinct()
                .forEach(System.out::println);

        //flatmap将mapper生成的流合并或者扁平到本流中
        System.out.println("flatmap..");
        List<String> strings2 = Arrays.asList("hello", "world");
        strings2.stream()
                .map(s -> s.split(""))
                .flatMap(Arrays::stream)//Arrays::stream将数组转换成流，flatmap将得到的流合并到主流中，扁平化流
                .distinct()
                .forEach(System.out::println);

        System.out.println("num pair..");//可见，简单的逻辑是顺序针对每个元素，执行每个lambda
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        numbers1.stream()
                .flatMap(i -> numbers2.stream()
                        .filter(j -> (i + j) % 3 == 0)
                        .map(j -> new int[]{i, j}))
                .forEach(arr -> System.out.println(Arrays.toString(arr)));
    }

    @Test
    public void testCommonUse3() {

        //都有短路效果，不用处理完流中所有数据，只要找到一个符合、不符合，即可
        //limit同样具有短路效果
        boolean isAnyMatchVegetarian = Data.menu.stream()
                .anyMatch(Dish::isVegetarian);
        System.out.println("isAnyMatchVegetarian:" + isAnyMatchVegetarian);

        boolean isAllMatchVegetarian = Data.menu.stream()
                .allMatch(Dish::isVegetarian);
        System.out.println("isAllMatchVegetarian:" + isAllMatchVegetarian);

        boolean isNoneMatchVegetarian = Data.menu.stream()
                .noneMatch(Dish::isVegetarian);
        System.out.println("isNoneMatchVegetarian:" + isNoneMatchVegetarian);

        Optional<Dish> any = Data.menu.stream()
                .filter(Dish::isVegetarian)
                .findAny();
        any.ifPresent(s -> System.out.println(s.getName()));

        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        integers.stream()
                .map(x -> x * x)
                .filter(x -> x % 3 == 0)
                .findFirst()
                .ifPresent(System.out::println);
    }

    //规约、fold
    @Test
    public void testCommonUse4() {

        List<Integer> integers = Arrays.asList(1, 2, 3, 4);
        //reduce将值(初始值或计算值)与序列中每一个元素使用lambda操作
        Integer sum = integers.stream()
                .reduce(0, Integer::sum);
        System.out.println("sum:" + sum);

        Integer max = integers.stream()
                .reduce(0, Integer::max);
        System.out.println("max:" + max);

        Integer count = Data.menu.stream()
                .map(d -> 1)
                .reduce(0, Integer::sum);
        long count1 = Data.menu.stream().count();
        System.out.println("count is same:" + (count == count1));
    }

    @Test
    public void testInAction() {

        Data.transactions.stream()
                .filter(transaction -> transaction.getYear() == 2011)
                .sorted(Comparator.comparingInt(Transaction::getValue))
                .forEach(System.out::println);

        System.out.println();
        Data.transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                //.distinct()
                //.forEach(System.out::println);
                .collect(Collectors.toSet());

        System.out.println();
        Data.transactions.stream()
                .map(Transaction::getTrader)
                .filter(t -> t.getCity().equals("Cambridge"))
                .distinct()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .forEach(System.out::println);

        System.out.println();
        String allName = Data.transactions.stream()
                .map(a -> a.getTrader().getName())
                .distinct()
                .sorted()
                //.reduce("", (a, b) -> a + b);//拼接，效率不高
                .collect(Collectors.joining());//stringbuilder
        System.out.println("all name:" + allName);

        System.out.println();
        boolean isInMilan = Data.transactions.stream()
                .anyMatch((a) -> a.getTrader().getCity().equals("Milan"));
        System.out.println("isInMilan:" + isInMilan);

        System.out.println();
        Data.transactions.stream()
                .filter((a) -> a.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .forEach(System.out::println);

        System.out.println();
        Optional<Integer> max = Data.transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max);
        System.out.println("max:" + max);

        System.out.println();
        Data.transactions.stream()
                .reduce((a, b) -> a.getValue() < b.getValue() ? a : b);
        Data.transactions.stream()
                .sorted(Comparator.comparingInt(Transaction::getValue))
                .findFirst()
                .ifPresent(System.out::println);
        Data.transactions.stream()
                .min(Comparator.comparingInt(Transaction::getValue));
    }

    //数值流
    @Test
    public void testIntStream() {

        Integer sum = Data.menu.stream()
                .map(Dish::getCalories)
                .reduce(0, Integer::sum);
        System.out.println("sum:" + sum);

        //避免拆装箱
        int sum2 = Data.menu.stream()
                .mapToInt(Dish::getCalories).sum();
        System.out.println("sum2:" + sum2);

        //装箱回去
        Stream<Integer> boxed = Data.menu.stream()
                .mapToInt(Dish::getCalories).boxed();
        boxed.forEach(System.out::println);

        //最大值避免是0,所以返回OptionalInt
        OptionalInt max = Data.menu.stream()
                .mapToInt(Dish::getCalories).max();
        System.out.println("max:" + max.orElse(1));

        long eventNums = IntStream.rangeClosed(1, 100)
                .filter(i -> i % 2 == 0)
                .count();
        System.out.println("eventNums:" + eventNums);

        //勾股数a * a + b * b = c * c,a、 b、 c都是整数，它们描述的正好是直角三角形的三条边长
        IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a ->
                        IntStream.rangeClosed(a, 100)//从a开始
                                .filter(b -> (Math.sqrt(a * a + b * b) % 1 == 0))//符合勾股定理
                                .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}))
                .limit(5)
                .forEach(x -> System.out.println(Arrays.toString(x)));
        //计算所有然后筛选，上面计算两次平方根。
        IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a ->
                        IntStream.rangeClosed(a, 100)
                                .mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)})
                                .filter(t -> t[2] % 1 == 0));
    }

    @Test
    public void testCreateStream() {

        Stream.of("a", "b", "c")
                .map(String::toUpperCase)
                .forEach(System.out::println);

        Stream<Object> empty = Stream.empty();

        int[] numbers = {1, 2, 3, 4};
        int sum = Arrays.stream(numbers).sum();
        System.out.println("sum:" + sum);

        Path path = Paths.get("/Users/lichao30/intellij_workspace/violin/violin-test/src/main/resources", "test.txt");
        try (Stream<String> lines = Files.lines(path, Charset.defaultCharset())) {
            long count = lines.flatMap(Stream::of)
                    .distinct()
                    .count();
            System.out.println("count:" + count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //无限流,依次对每个新生成的值应用函数的
        Stream.iterate(0, n -> n + 2)
                .limit(5)
                .forEach(System.out::println);

        //斐波纳契数列-开始的两个数字是0和1，后续的每个数字都是前两个数字之和
        //斐波纳契元组序列，是数列中数字和其后续数字组成的元组构成的序列： (0, 1),(1, 1), (1, 2), (2, 3), (3, 5)
        Stream.iterate(new int[]{0, 1}, n -> new int[]{n[1], n[0] + n[1]})
                .limit(5)
                .forEach(a -> System.out.println(Arrays.toString(a)));

        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);

        Stream.generate(() -> 1)
                .limit(5)
                .forEach(System.out::println);

        //仅仅演示，因为有操作有状态，不易并行
        IntSupplier fib = new IntSupplier() {
            private int previous = 0;
            private int current = 1;

            public int getAsInt() {
                int oldPrevious = this.previous;
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
                return oldPrevious;
            }
        };
        IntStream.generate(fib).limit(10).forEach(System.out::println);

    }
}

package com.wolf.test.jdknewfuture.java8inaction;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/07/22
 */
public class CollectorTest {

    @Test
    public void testAggregate() {

        Map<Integer, List<Transaction>> group = Data.transactions.stream()
                .collect(groupingBy(Transaction::getCurrency));
        System.out.println(group);

        Data.menu.stream().collect(counting());
        Data.menu.stream().count();
        Integer integer = Data.menu.stream().map(Dish::getCalories).reduce(Integer::sum).orElse(0);
        Integer integer2 = Data.menu.stream().mapToInt(Dish::getCalories).sum();//建议

        Optional<Dish> max = Data.menu.stream()
                .collect(maxBy(comparingInt(Dish::getCalories)));
        max.ifPresent(System.out::println);
        Optional<Dish> max2 = Data.menu.stream().max(comparingInt(Dish::getCalories));

        Integer sum = Data.menu.stream().collect(summingInt(Dish::getCalories));
        Integer sum2 = Data.menu.stream().mapToInt(Dish::getCalories).sum();

        Double avg = Data.menu.stream().collect(averagingInt(Dish::getCalories));

        IntSummaryStatistics summary = Data.menu.stream().collect(summarizingInt(Dish::getCalories));
        System.out.println("summary:" + summary);

        String joining = Data.menu.stream().map(Dish::getName).collect(joining());
        System.out.println("joining:" + joining);
        String joining2 = Data.menu.stream().map(Dish::getName).collect(joining(", "));
        System.out.println("joining2:" + joining2);

        //从初始值，对每个元素，转换Dish::getCalories，再累加(i, j) -> i + j
        Integer collect = Data.menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j));
        Integer collect2 = Data.menu.stream().collect(reducing(0, Dish::getCalories, Integer::sum));

        Data.menu.stream().collect(reducing((i, j) -> i.getCalories() > j.getCalories() ? i : j));

        //转换成list-错误，不能并行工作
        //reduce方法旨在把两个值结合起来生成一个新值，它是一个不可变的归约
        //collect方法的设计就是要改变容器，从而累积要输出的结果。
        List<Integer> reduce = Arrays.asList(1, 2, 3, 4).stream()
                .reduce(new ArrayList<>(),
                        (List<Integer> l, Integer e) -> {
                            l.add(e);
                            return l;
                        }, (List<Integer> l1, List<Integer> l2) -> {
                            l1.addAll(l2);
                            return l1;
                        });
        System.out.println(reduce);
    }

    @Test
    public void testGroup() {

        Map<Dish.Type, List<Dish>> group1 = Data.menu.stream().collect(groupingBy(Dish::getType));
        System.out.println("group1:" + group1);

        Map<CaloricLevel, List<Dish>> group2 = Data.menu.stream().collect(groupingBy(d -> {
            if (d.getCalories() <= 400) {
                return CaloricLevel.DIET;
            } else if (d.getCalories() <= 700) {
                return CaloricLevel.NORMAL;
            } else {
                return CaloricLevel.FAT;
            }
        }));
        System.out.println("group2:" + group2);

        //子分组进一步分组
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> group3 = Data.menu.stream().collect(
                groupingBy(Dish::getType,
                        groupingBy(d -> {
                            if (d.getCalories() <= 400) {
                                return CaloricLevel.DIET;
                            } else if (d.getCalories() <= 700) {
                                return CaloricLevel.NORMAL;
                            } else {
                                return CaloricLevel.FAT;
                            }
                        })));
        System.out.println("group3:" + group3);

        Map<Dish.Type, Long> group4 = Data.menu.stream().collect(
                groupingBy(Dish::getType, counting()));
        System.out.println("group4:" + group4);

        Map<Dish.Type, Optional<Dish>> group5 = Data.menu.stream().collect(
                groupingBy(Dish::getType, maxBy(comparingInt(Dish::getCalories))));
        System.out.println("group5:" + group5);

        //去掉optional
        Map<Dish.Type, Dish> group6 = Data.menu.stream().collect(
                groupingBy(Dish::getType,
                        collectingAndThen(maxBy(comparingInt(Dish::getCalories)), Optional::get)));
        System.out.println("group6:" + group6);

        Map<Dish.Type, Integer> group7 = Data.menu.stream().collect(
                groupingBy(Dish::getType,
                        summingInt(Dish::getCalories)));
        System.out.println("group7:" + group7);

        //mapping，进一步归约，收集
        Map<Dish.Type, Set<CaloricLevel>> group8 = Data.menu.stream().collect(
                groupingBy(Dish::getType,
                        mapping(d -> {
                            if (d.getCalories() <= 400) {
                                return CaloricLevel.DIET;
                            } else if (d.getCalories() <= 700) {
                                return CaloricLevel.NORMAL;
                            } else {
                                return CaloricLevel.FAT;
                            }
                        }, toSet())));
        System.out.println("group8:" + group8);

        //输出为set类型
        Data.menu.stream().collect(
                groupingBy(Dish::getType,
                        mapping(d -> {
                            if (d.getCalories() <= 400) {
                                return CaloricLevel.DIET;
                            } else if (d.getCalories() <= 700) {
                                return CaloricLevel.NORMAL;
                            } else {
                                return CaloricLevel.FAT;
                            }
                        }, toCollection(HashSet::new))));
        System.out.println("group8:" + group8);

        //使用groupingBy比较直观知道你要对什么分组，然后对value再转换
        Map<String, List<Integer>> collect = Data.menu.stream()
                .collect(groupingBy(
                        m -> m.getName(),
                        mapping(
                                m -> m.getCalories(),
                                toList()
                        )));
        System.out.println(collect);
    }

    //使用最单纯的Collector.of查看原理。推荐就用mapping了
    //groupingBy的Function<? super T, ? extends K> classifier用来生成key，
    //Collector<? super T, A, D> downstream用来产生Collector需要的函数
    //整体返回的是Collector
    //当执行collect时就会循环调用Collector中的方法
    @Test
    public void testSourceCode() {
        Data.menu.stream().collect(
                groupingBy(Dish::getType,
                        Collector.of(HashSet::new,
                                (Set<CaloricLevel> l, Dish d) -> {
                                    CaloricLevel caloricLevel;
                                    if (d.getCalories() <= 400) {
                                        caloricLevel = CaloricLevel.DIET;
                                    } else if (d.getCalories() <= 700) {
                                        caloricLevel = CaloricLevel.NORMAL;
                                    } else {
                                        caloricLevel = CaloricLevel.FAT;
                                    }

                                    l.add(caloricLevel);
                                },
                                (Set<CaloricLevel> left, Set<CaloricLevel> right) -> {
                                    left.addAll(right);
                                    return left;
                                }
                        )));
    }

    @Test
    public void testGroupSort() {
        //3 apple, 2 banana, others 1
        List<String> items =
                Arrays.asList("apple", "apple", "banana",
                        "apple", "orange", "banana", "papaya");
        Map<String, Long> result = items
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(result);

        Map<String, Long> finalMap = new LinkedHashMap<>();
        //reversed 反排序
        result.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
        System.out.println(finalMap);
    }


    enum CaloricLevel {DIET, NORMAL, FAT}

    @Test
    public void testPartition() {

        //partitioningBy需要一个谓词,分 区 看 作 分 组 一 种 特 殊 情 况
        Map<Boolean, List<Dish>> part1 = Data.menu.stream().collect(partitioningBy(Dish::isVegetarian));
        System.out.println("part1:" + part1);
        Map<Boolean, List<Dish>> group1 = Data.menu.stream().collect(groupingBy((Dish::isVegetarian)));
        System.out.println("group1:" + group1);
        List<Dish> toList1 = Data.menu.stream().filter(Dish::isVegetarian).collect(toList());
        System.out.println("toList1:" + toList1);

        Map<Boolean, Map<Dish.Type, List<Dish>>> secondGroup = Data.menu.stream()
                .collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));

        Data.menu.stream().collect(partitioningBy(Dish::isVegetarian,
                collectingAndThen(maxBy(comparingInt(Dish::getCalories)), Optional::get)));

        //isprime
        IntStream.range(0, 20).boxed().collect(partitioningBy(n -> isPrime(n)));
    }

    private boolean isPrime(int n) {
        int sqrt = (int) Math.sqrt(n);
        return IntStream.rangeClosed(2, sqrt)
                .noneMatch(i -> n % i == 0);
    }

    @Test
    public void testCustomCollector() {

        //jdk
        Data.menu.stream().collect(Collectors.toList());

        //自定义
        Data.menu.stream().collect(ToListCollector.toList());

        //lambda
        Data.menu.stream().collect(
                () -> new ArrayList<>(),
                (List<Dish> l, Dish a) -> {
                    l.add(a);
                },
                (List<Dish> l, List<Dish> r) -> {
                    l.addAll(r);
                }
        );

        Data.menu.stream().collect(
                ArrayList::new,
                List::add,
                List::addAll
        );

        ElapseTimeTest.measureElapse(() ->
                IntStream.rangeClosed(2, Integer.MAX_VALUE).boxed()
                        .collect(partitioningBy(n -> isPrime((Integer) n))));

        ElapseTimeTest.measureElapse(() ->
                IntStream.rangeClosed(2, Integer.MAX_VALUE).boxed()
                        .collect(new PrimeNumbersCollector()));

        IntStream.range(0, 20).boxed().collect(
                () -> new HashMap<Boolean, List<Integer>>() {
                    {
                        put(false, new ArrayList<>());
                        put(true, new ArrayList<>());
                    }
                },
                (HashMap<Boolean, List<Integer>> acc, Integer i) ->
                        acc.get(PrimeNumbersCollector.isPrime(acc.get(true), i))
                                .add(i),
                (HashMap<Boolean, List<Integer>> l, HashMap<Boolean, List<Integer>> r) ->
                {
                    l.get(false).addAll(r.get(false));
                    l.get(true).addAll(r.get(true));
                }
        );
    }

    @Test
    public void testToMap() {

        //一般转换map存在2个问题：1.对于重复key报错  2.对于value为null报错
//      Map<String, Integer> mapx = Data.menu.stream()
//          .collect(Collectors.toMap(Dish::getName, Dish::getCalories));
//      System.out.println(mapx);

        //解决方式1
        Map<String, Integer> map1 = Data.menu.stream()
                .collect(HashMap::new, (map, menu) ->
                        map.put(menu.getName(), menu.getCalories()), Map::putAll);
        System.out.println("map1:" + map1);

        //解决方式2
        Map<String, Integer> map2 = Data.menu.stream()
                .collect(Collector.of(
                        HashMap::new,
                        (map, menu) -> map.put(menu.getName(), menu.getCalories()),
                        (hmap1, hmap2) -> {
                            throw new UnsupportedOperationException();
                        }
                        , new Collector.Characteristics[]{Collector.Characteristics.IDENTITY_FINISH}));
        System.out.println("map2:" + map2);
    }
}
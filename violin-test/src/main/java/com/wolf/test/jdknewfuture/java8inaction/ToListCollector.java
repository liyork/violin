package com.wolf.test.jdknewfuture.java8inaction;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Description: 自定义实现tolist的Collector
 *
 * @author 李超
 * @date 2019/07/23
 */
public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {//流中元素类型、累加器类型、收集结果类型

    //容器
    @Override
    public Supplier<List<T>> supplier() {
        return ArrayList::new;//给accumulator使用
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return List::add;//收集数据
    }

    //并行时使用
    @Override
    public BinaryOperator<List<T>> combiner() {
        return (a, b) -> {//组合两个流
            a.addAll(b);
            return a;
        };
    }

    //最终执行
    @Override
    public Function<List<T>, List<T>> finisher() {
        return Function.identity();//原样返回
    }

    //特性
    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(
                EnumSet.of(Characteristics.IDENTITY_FINISH,//标识accumulator就是收集结果类型
                        Characteristics.CONCURRENT));//可并发,前提是数据无序
    }

    public static Collector toList() {
        return new ToListCollector<>();
    }
}

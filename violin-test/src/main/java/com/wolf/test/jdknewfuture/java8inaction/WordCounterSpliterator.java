package com.wolf.test.jdknewfuture.java8inaction;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Description: 单词切分器
 * Spliterator为了并行执行而设计，可以控制stream拆分数据结构的策略
 *
 * @author 李超
 * @date 2019/07/25
 */
public class WordCounterSpliterator implements Spliterator<Character> {
    private final String string;
    private int currentChar = 0;

    public WordCounterSpliterator(String string) {
        this.string = string;
    }

    //有需要处理的元素则执行action，返回true，否则false
    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(string.charAt(currentChar++));
        return currentChar < string.length();
    }

    //划分元素给新的Spliterator，用于stream的并发执行。stream调用此方法是个递归。默认stream以任意位置拆分
    @Override
    public Spliterator<Character> trySplit() {
        int currentSize = string.length() - currentChar;
        if (currentSize < 10) {//数据量不够则不切分。
            return null;
        }

        //尝试从currentSize / 2 + currentChar位置开始切分，找到第一个空格，新建Spliterator处理currentChar--splitPos之间数据，自己处理currentChar后续的
        for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
            if (Character.isWhitespace(string.charAt(splitPos))) {
                Spliterator<Character> spliterator =
                        new WordCounterSpliterator(string.substring(currentChar, splitPos));
                currentChar = splitPos;
                return spliterator;
            }
        }

        return null;
    }

    //剩余可遍历元素
    @Override
    public long estimateSize() {
        return string.length() - currentChar;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}
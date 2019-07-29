package com.wolf.test.jdknewfuture.java8inaction;

/**
 * Description: 单词计数，用于reduce时，保存中间过程。无状态，可并发
 *
 * @author 李超
 * @date 2019/07/25
 */
public class WordCounter {

    private final int count;
    private final boolean lastSpace;

    public WordCounter(int count, boolean lastSpace) {
        this.count = count;
        this.lastSpace = lastSpace;
    }

    public WordCounter accumulate(Character c) {
        if (Character.isWhitespace(c)) {//遇到空格且前一个也是空格则持续向后，若不是空格则保存count并重新开始
            return lastSpace ?
                    this :
                    new WordCounter(count, true);
        } else {
            return lastSpace ?
                    new WordCounter(count + 1, false) :
                    this;
        }
    }

    //合并两个并行counter
    public WordCounter combine(WordCounter wordCounter) {
        return new WordCounter(count + wordCounter.count, wordCounter.lastSpace);
    }

    public int getCount() {
        return count;
    }
}

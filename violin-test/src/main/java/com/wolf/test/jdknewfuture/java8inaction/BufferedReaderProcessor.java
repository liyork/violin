package com.wolf.test.jdknewfuture.java8inaction;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/07/19
 */
@FunctionalInterface
public interface BufferedReaderProcessor {

    //显示抛出异常，lambda内不用捕获，外层需要捕获。
    String read(BufferedReader bufferedReader) throws IOException;
}

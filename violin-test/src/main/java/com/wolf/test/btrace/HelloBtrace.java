package com.wolf.test.btrace;


import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;

import static com.sun.btrace.BTraceUtils.println;

/**
 * Description:
 * D:\intellijWrkSpace\violin\violin-test\src\main\java\com\wolf\test\btrace>btrace 23192 HelloBtrace.java
 * <br/> Created on 2018/1/26 11:22
 *
 * @author 李超
 * @since 1.0.0
 */
@BTrace(unsafe = true)
public class HelloBtrace {

    @OnMethod(
            clazz = "com.wolf.test.btrace.RemoteClass",
            method = "f1"
    )
    public static void onF1() {
        System.out.println("Hello BTrace");//打印到intellij控制台
        println("Hello BTrace");//打印到命令行控制台
    }
}

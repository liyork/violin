package com.wolf.test.btrace;

import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.strcat;

/**
 * Description:
 * D:\intellijWrkSpace\violin\violin-test\src\main\java\com\wolf\test\btrace>btrace -cp D:\intellijWrkSpace\violin\violin-test\target\classes 26520 TraceMethodLine.java
 * <br/> Created on 2018/1/26 17:05
 *
 * @author 李超
 * @since 1.0.0
 */
@BTrace(unsafe = true)
public class TraceMethodLine {

    @OnMethod(
            clazz = "com.wolf.test.btrace.CaseObject",
            location = @Location(value = Kind.LINE, line = 15)//注释也算行，已intellij中展示为准，没有执行这行则下面不会打印
    )
    public static void traceExecute(@ProbeClassName String pcn, @ProbeMethodName String pmn, int line) {
        System.out.println(strcat(strcat(strcat("call ", pcn), "."), pmn));
    }
}

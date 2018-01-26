package com.wolf.test.btrace;

import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.*;

/**
 * Description:
 * D:\intellijWrkSpace\violin\violin-test\src\main\java\com\wolf\test\btrace>btrace -cp D:\intellijWrkSpace\violin\violin-test\target\classes 35744 TraceMethodArgsAndReturn.java
 * <br/> Created on 2018/1/26 16:59
 *
 * @author 李超
 * @since 1.0.0
 */
@BTrace(unsafe = true)
public class TraceMethodExecuteTime {

    @TLS
    static long beginTime;

    //开始
    @OnMethod(
            clazz = "com.wolf.test.btrace.CaseObject",
            method = "execute"
    )
    public static void traceExecuteBegin() {
        beginTime = timeMillis();
    }

    //结束
    @OnMethod(
            clazz = "com.wolf.test.btrace.CaseObject",
            method = "execute",
            location = @Location(Kind.RETURN)
    )
    public static void traceExecute(int sleepTime, @Return boolean result) {
        System.out.println(strcat(strcat("com.wolf.test.btrace.CaseObject.execute time is:", str(timeMillis() - beginTime)), "ms"));
    }
}

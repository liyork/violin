package com.wolf.test.btrace;

import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.Reflective.field;
import static com.sun.btrace.BTraceUtils.Reflective.get;
import static com.sun.btrace.BTraceUtils.Strings.str;
import static com.sun.btrace.BTraceUtils.Strings.strcat;
import static com.sun.btrace.BTraceUtils.println;

/**
 * Description:
 * D:\intellijWrkSpace\violin\violin-test\src\main\java\com\wolf\test\btrace>btrace -cp D:\intellijWrkSpace\violin\violin-test\target\classes 35744 TraceMethodArgsAndReturn.java
 * <br/> Created on 2018/1/26 16:38
 *
 * @author 李超
 * @since 1.0.0
 */
@BTrace(unsafe = true)
public class TraceMethodArgsAndReturn {

    @OnMethod(
            clazz = "com.wolf.test.btrace.CaseObject",
            method = "execute",
            location = @Location(Kind.RETURN)
    )
    public static void traceExecute(@Self CaseObject instance, int sleepTime, @Return boolean result) {
        println("call com.wolf.test.btrace.CaseObject.execute");
        println(strcat("sleepTime is:", str(sleepTime)));
        println(strcat("sleepTotalTime is:", str(get(field("com.wolf.test.btrace.CaseObject", "sleepTotalTime"), instance))));
        println(strcat("return value is:", str(result)));
    }
}

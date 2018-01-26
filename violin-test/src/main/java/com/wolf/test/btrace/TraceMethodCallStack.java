package com.wolf.test.btrace;

import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;

import static com.sun.btrace.BTraceUtils.jstack;
import static com.sun.btrace.BTraceUtils.println;

/**
 * Description:
 * D:\intellijWrkSpace\violin\violin-test\src\main\java\com\wolf\test\btrace>btrace -cp D:\intellijWrkSpace\violin\violin-test\target\classes 35824 TraceMethodCallStack.java
 * <br/> Created on 2018/1/26 17:03
 *
 * @author 李超
 * @since 1.0.0
 */
@BTrace(unsafe = true)
public class TraceMethodCallStack {

    @OnMethod(
            clazz = "com.wolf.test.btrace.CaseObject",
            method = "execute"
    )
    public static void traceExecute() {
        println("who call CaseObject.execute :");
        jstack();
    }
}

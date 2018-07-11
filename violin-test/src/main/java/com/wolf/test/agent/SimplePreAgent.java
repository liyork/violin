package com.wolf.test.agent;

import java.lang.instrument.Instrumentation;

/**
 * Description:测试PreAgent
 *
 * agent = pre main
 * jar cvfm presimpleagent.jar agent/META-INF/MANIFEST.MF com\wolf\test\agent\SimplePreAgent.class
 * 或：jar cvfm presimpleagent.jar agent/META-INF/MANIFEST.MF com/wolf/test/agent/SimplePreAgent.class
 *
 * jar cvfm hot.jar presimpleagent/META-INF/MANIFEST.MF com\wolf\test\jvm\loadclass\Hot2.class
 * 或：jar cvfm hot.jar presimpleagent/META-INF/MANIFEST.MF com/wolf/test/jvm/loadclass/Hot2.class
 *
 * java -javaagent:presimpleagent.jar=Hello1 -jar hot.jar
 * java -javaagent:presimpleagent.jar=Hello1 -javaagent:myagent.jar=Hello2 -jar hot.jar
 * <p>
 * <br/> Created on 2018/1/22 18:17
 *
 * @author 李超
 * @since 1.0.0
 */
public class SimplePreAgent {

    /**
     * 该方法在main方法之前运行，与main方法运行在同一个JVM中
     * 并被同一个System ClassLoader装载
     * 被统一的安全策略(security policy)和上下文(context)管理
     *
     * @param agentOps
     * @param inst
     * @author SHANHY
     * @create 2016年3月30日
     */
    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("=========premain方法执行========");
        System.out.println("agentOps:"+agentOps);
        Thread thread = Thread.currentThread();
        System.out.println("currentThread:"+thread.getName());
        StackTraceElement[] stackTrace = thread.getStackTrace();
        System.out.println("stackTrace.length:"+(null!=stackTrace?stackTrace.length:null));
        for (StackTraceElement stackTraceElement : stackTrace) {
            System.out.println(stackTraceElement);
        }
    }

    /**
     * 如果不存在 premain(String agentOps, Instrumentation inst)
     * 则会执行重载方法 premain(String agentOps)
     *
     * @param agentOps
     * @author SHANHY
     * @create 2016年3月30日
     */
    public static void premain(String agentOps) {
        System.out.println("=========premain方法执行2========");
        System.out.println(agentOps);
    }
}

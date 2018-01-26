package com.wolf.test.agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * Description:
 * D:\intellijWrkSpace\violin\violin-test\target\classes>jar cvfm simpleagentmain.jar simpleagentmain/META-INF/MANIFEST.MF com\wolf\test\agent\SimpleAgentMain.class
 * <br/> Created on 2018/1/24 10:01
 *
 * @author 李超
 * @since 1.0.0
 */
public class SimpleAgentMain {

    public static void agentmain(String args, Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("SimpleAgentMain agentmain attach...");

        System.out.println("args:" + args);
        System.getProperties().setProperty("monitor.conf", args);

        for (Class clazz : inst.getAllLoadedClasses()) {
            System.out.println(clazz.getName());
        }

        System.out.println("SimpleAgentMain agentmain end...");
    }
}

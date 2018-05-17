package com.wolf.test.agent;

import com.wolf.test.agent.targetobj.TimeTest;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * Description:
 * 由于java是解释性语言，可能调用对象方法时，会关联到class对象进而找到方法区中的方法然后执行，这时对于已经retransform的类就用新方法了。
 * <p>
 * jar cvfm agentmain.jar agentmain/META-INF/MANIFEST.MF com\wolf\test\agent\AgentMain.class com\wolf\test\agent\AgentMainTransformer.class
 * <br/> Created on 2018/1/24 10:01
 *
 * @author 李超
 * @since 1.0.0
 */
public class AgentMain {

    public static void agentmain(String args, Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("AgentMain agentmain attach...");

        System.out.println("args:" + args);
        System.getProperties().setProperty("monitor.conf", args);

        for (Class clazz : inst.getAllLoadedClasses()) {
            //System.out.println(clazz.getName());
        }
        inst.addTransformer(new AgentMainTransformer(), true);
        inst.retransformClasses(TimeTest.class);//对于已经加载的类重新进行转换处理，即会触发重新加载类定义，需要注意的是，新加载的类不能修改旧有的类声明，譬如不能增加属性、不能修改方法声明

        System.out.println("AgentMain agentmain end...");
    }
}

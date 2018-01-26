package com.wolf.test.agent;

import java.lang.instrument.Instrumentation;

/**
 * Description:
 * D:\intellijWrkSpace\violin\violin-test\target\classes>jar cvfm preAgentTransformer.jar preAgentTransformer/META-INF/MANIFEST.MF com\wolf\test\agent\PreAgentTransformer.class com\wolf\test\agent\PreTransformer.class
 * D:\intellijWrkSpace\violin\violin-test\target\classes>jar cvfm time.jar  timetest/META-INF/MANIFEST.MF com\wolf\test\agent\targetobj\TimeTest.class
 * <p>
 * java -javaagent:preAgentTransformer.jar=Hello1 -jar time.jar
 * <p>
 * <br/> Created on 2018/1/22 18:17
 *
 * @author 李超
 * @since 1.0.0
 */
public class PreAgentTransformer {

    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("=========premain方法执行========");
        System.out.println(agentOps);
        // 添加Transformer
        inst.addTransformer(new PreTransformer());
    }
}

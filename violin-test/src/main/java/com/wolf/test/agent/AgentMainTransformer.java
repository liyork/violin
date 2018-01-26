package com.wolf.test.agent;

import com.alibaba.fastjson.JSON;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <br/> Created on 2018/1/22 19:39
 *
 * @author 李超
 * @since 1.0.0
 */
public class AgentMainTransformer implements ClassFileTransformer {

    final static String prefix = "\n startTime = System.currentTimeMillis();\n";
    final static String postfix = "\n endTime = System.currentTimeMillis();\n";

    // 被处理的方法列表
    final static Map<String, List<String>> methodMap = new HashMap<String, List<String>>();

    public AgentMainTransformer() {
        add("com.wolf.test.agent.targetobj.TimeTest.sayHello");
        add("com.wolf.test.agent.targetobj.TimeTest.sayHello2");
    }

    private void add(String methodString) {
        String className = methodString.substring(0, methodString.lastIndexOf("."));
        String methodName = methodString.substring(methodString.lastIndexOf(".") + 1);
        List<String> list = methodMap.get(className);
        if (list == null) {
            list = new ArrayList<String>();
            methodMap.put(className, list);
        }
        list.add(methodName);
    }

    //loadclass之前，新增方法，对原有方法进行包装返回给classloader
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        for (StackTraceElement stackElement : stackElements) {
            System.out.println(JSON.toJSONString(stackElement));
        }

        System.out.println("run transform:...loader:" + loader + ",className:" + className);
        className = className.replace("/", ".");
        //System.out.println("className:" + className);
        if (methodMap.containsKey(className)) {// 判断加载的class的包路径是不是需要监控的类
            CtClass ctclass = null;
            try {
                ctclass = ClassPool.getDefault().get(className);// 使用全称,用于取得字节码类<使用javassist>
                for (String methodName : methodMap.get(className)) {
                    String outputStr = "\nSystem.out.println(\"this method " + methodName
                            + " cost:\" +(endTime - startTime) +\"ms.\");";


                    CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);
                    ctmethod.addLocalVariable("startTime", CtClass.longType);
                    ctmethod.addLocalVariable("endTime", CtClass.longType);
                    ctmethod.insertBefore(prefix);
                    ctmethod.insertAfter(postfix);
                    ctmethod.insertAfter(outputStr);

                }
                return ctclass.toBytecode();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }
}
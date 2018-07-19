package com.wolf.test.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import sun.instrument.InstrumentationImpl;
import sun.instrument.TransformerManager;
import sun.launcher.LauncherHelper;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:preagent不能修改原有方法内容，只能换名字。
 * 1.原始方法改名，sayHello$old
 * 2.拷贝原有方法，添加打印日志，调用sayHello$old
 * <br/> Created on 2018/1/22 19:39
 *
 * @author 李超
 * @since 1.0.0
 */
public class PreTransformer implements ClassFileTransformer {

    final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
    final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";

    // 被处理的方法列表
    final static Map<String, List<String>> methodMap = new HashMap<String, List<String>>();

    public PreTransformer() {
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
    //构造新方法加入begin、end，替换老方法调用新方法
    //该方法会在加载新class类或者重新加载class类时，调用

//    java.lang.Thread.getStackTrace(Thread.java:1559)
//            com.wolf.test.agent.PreTransformer.transform(PreTransformer.java:59)
//            sun.instrument.TransformerManager.transform(TransformerManager.java:188)
//            sun.instrument.InstrumentationImpl.transform(InstrumentationImpl.java:428)
//            java.lang.ClassLoader.defineClass1(Native Method)
//            java.lang.ClassLoader.defineClass(ClassLoader.java:763)
//            java.security.SecureClassLoader.defineClass(SecureClassLoader.java:142)
//            java.net.URLClassLoader.defineClass(URLClassLoader.java:467)
//            java.net.URLClassLoader.access$100(URLClassLoader.java:73)
//            java.net.URLClassLoader$1.run(URLClassLoader.java:368)
//            java.net.URLClassLoader$1.run(URLClassLoader.java:362)
//            java.security.AccessController.doPrivileged(Native Method)
//            java.net.URLClassLoader.findClass(URLClassLoader.java:361)
//            java.lang.ClassLoader.loadClass(ClassLoader.java:424)
//            sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:335)
//            java.lang.ClassLoader.loadClass(ClassLoader.java:357)
//            sun.launcher.LauncherHelper.checkAndLoadMain(LauncherHelper.java:495)
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println("run transform:...loader:" + loader + ",className:" + className);
        className = className.replace("/", ".");
        //System.out.println("className:" + className);
        if (methodMap.containsKey(className)) {// 判断加载的class的包路径是不是需要监控的类

//            System.out.println("Thread.currentThread().getName():"+Thread.currentThread().getName());
//            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//            System.out.println("stackTrace:"+(null!=stackTrace?stackTrace.length:null));
//            for (StackTraceElement stackTraceElement : stackTrace) {
//                System.out.println(stackTraceElement);
//            }

            CtClass ctclass = null;
            try {
                ctclass = ClassPool.getDefault().get(className);// 使用全称,用于取得字节码类<使用javassist>
                for (String methodName : methodMap.get(className)) {
                    String outputStr = "\nSystem.out.println(\"this method " + methodName
                            + " cost:\" +(endTime - startTime) +\"ms.\");";

                    CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);
                    String newMethodName = methodName + "$old";// 新定义一个方法,比如sayHello$old
                    ctmethod.setName(newMethodName);// 将原来的方法名字修改

                    // 创建新的方法，复制原来的方法，名字为原来的名字
                    CtMethod newMethod = CtNewMethod.copy(ctmethod, methodName, ctclass, null);

                    // 构建新的方法体
                    StringBuilder bodyStr = new StringBuilder();
                    bodyStr.append("{");
                    bodyStr.append(prefix);
                    bodyStr.append(newMethodName + "($$);\n");// 调用原有代码，类似于method();($$)表示所有的参数
                    bodyStr.append(postfix);
                    bodyStr.append(outputStr);
                    bodyStr.append("}");


                    newMethod.setBody(bodyStr.toString());// 替换新方法
                    ctclass.addMethod(newMethod);// 增加新方法
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
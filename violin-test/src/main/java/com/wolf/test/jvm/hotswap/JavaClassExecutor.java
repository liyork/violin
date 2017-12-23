package com.wolf.test.jvm.hotswap;

import com.sun.xml.internal.ws.api.model.MEP;

import java.lang.reflect.Method;

/**
 * Description:
 * <br/> Created on 11/5/17 7:52 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class JavaClassExecutor {

    //替换目标类中输出类常量为指定的输出常量
    public static String execute(byte[] classByte) {
        HackSystem.clearBuffer();
        ClassModifier cm = new ClassModifier(classByte);
        byte[] modiBytes = cm.modifyUTF8Constant("java/lang/System", "com/wolf/test/jvm/hotswap/HackSystem");
        //每次都使用新的类加载器，保证每次方法调用时都加载一个新类
        HotSwapClassLoader hotSwapClassLoader = new HotSwapClassLoader();
        Class clazz = hotSwapClassLoader.loadByte(modiBytes);
        try {
            //获取目标类的静态main方法
            Method method = clazz.getMethod("main", new Class[]{String[].class});
            method.invoke(null, new String[]{null});

        } catch (Throwable throwable) {
            throwable.printStackTrace(HackSystem.out);
        }
        return HackSystem.getBufferString();
    }
}

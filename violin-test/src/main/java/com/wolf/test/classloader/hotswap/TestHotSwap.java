package com.wolf.test.classloader.hotswap;

import java.io.FileInputStream;
import java.net.URL;

/**
 * Description:替换输出类
 * <br/> Created on 11/5/17 7:55 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class TestHotSwap {

    public static void main(String[] args) throws Exception {

        URL resourceFromClassPath = TestHotSwap.class.getClassLoader().getResource("com/wolf/test/classloader/hotswap/TargetServerClass.class");
        FileInputStream fileInputStream = new FileInputStream(resourceFromClassPath.getPath());
        byte[] b = new byte[fileInputStream.available()];
        fileInputStream.read(b);
        fileInputStream.close();

        JavaClassExecutor.execute(b);

        String bufferString = HackSystem.getBufferString();
        System.out.println("bufferString:"+bufferString);
    }
}

package com.wolf.test.classloader.basetest;

import java.io.*;

/**
 * Description:
 * <br/> Created on 2018/6/20 11:38
 *
 * @author 李超
 * @since 1.0.0
 */
public class SecureTest {

    public static void test(String path) {
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(path + "en");
            int b = 0;
            int b1 = 0;
            try {
                while ((b = fis.read()) != -1) {
                    //每一个byte异或一个数字2
                    fos.write(b ^ 2);
                }
                fos.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SecureTest.test("D:\\tmp\\NonClassPathClass.class");
    }
}

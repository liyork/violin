package com.wolf.test.base;

import com.wolf.utils.FileUtils;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Description:
 * <br/> Created on 2017/5/1 7:17
 *
 * @author 李超
 * @since 1.0.0
 */
public class FileUtilsTest {

    public static void main(String[] args) {
        File file = new File("D:\\pmsoftwares");
        for (File file1 : file.listFiles()) {
            System.out.println(file1.getName());
        }
//        copyFile(file);

        //FileUtils.printFileName("D:\\apache-maven-3.5.0");
    }

    private static void copyFile(File file) {

        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return true;
            }
        };
//        File[] files = file.listFiles(filter);
//
//        if(ArrayUtils.isEmpty(files)) {
//            return ;
//        }
//        System.out.println(files.length);
//
//        for(File file1 : files) {
//            String name = file1.getName();
//            String substring = name.substring(0,name.lastIndexOf("."))+".jad";
//            File file2 = new File("D:\\temp1\\" + substring);

        File sourceDir = new File("D:\\temp");
        File directDir = new File("D:\\temp1");
        FileUtils.copyFile(sourceDir, directDir, filter);
//        }`
    }

    //从classpath中的jar找到符合的文件
    //把/Users/lichao30/intellij_workspace/violin/violin-test/target/classes/hot3.jar添加到classpath中
    @Test
    public void testLoadFromJar() throws Exception {

        //指定jar获取
        JarFile jarFile = new JarFile("/Users/lichao30/intellij_workspace/violin/violin-test/target/classes/hot3.jar");
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.getName().equals("testloadjar.properties")) {
                InputStream inputStream = jarFile.getInputStream(jarEntry);
                readFromInputStream(inputStream);
            }
        }

        //从classpath中获取
        System.out.println();
        Enumeration<URL> resources = FileUtilsTest.class.getClassLoader().getResources("testloadjar.properties");
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();

            InputStream in = url.openStream();
            readFromInputStream(in);
        }

        System.out.println();
        //只读取第一个
        InputStream resourceAsStream = FileUtilsTest.class.getClassLoader().getResourceAsStream("testloadjar.properties");
        readFromInputStream(resourceAsStream);
    }

    private void readFromInputStream(InputStream in) throws IOException {
        Reader f = new InputStreamReader(in);
        BufferedReader fb = new BufferedReader(f);
        StringBuffer sb = new StringBuffer("");
        String s = "";
        while ((s = fb.readLine()) != null) {
            sb = sb.append(s);
        }

        fb.close();
        f.close();

        System.out.println("sb==>" + sb);
    }
}

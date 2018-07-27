package com.wolf.test.base;

import com.wolf.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;

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
        FileUtils.copyFile(sourceDir,directDir,filter);
//        }`
    }
}

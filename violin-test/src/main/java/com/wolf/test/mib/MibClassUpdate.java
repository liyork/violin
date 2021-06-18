package com.wolf.test.mib;

import org.gjt.jclasslib.io.ClassFileWriter;
import org.gjt.jclasslib.structures.ClassFile;
import org.gjt.jclasslib.structures.Constant;
import org.gjt.jclasslib.structures.InvalidByteCodeException;
import org.gjt.jclasslib.structures.constants.ConstantLongInfo;

import java.io.*;
import java.util.HashSet;

/**
 * Description:
 * 需要工程中引入violin-test/lib包
 * 将所需class放入browser目录下
 * Created on 2021/6/18 5:23 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class MibClassUpdate {

    static HashSet<String> filePath = new HashSet<>();

    public static void main(String[] args) {
        String dir = "/Users/chaoli/intellijWrkSpace/violin/violin-test/src/main/resources/browser";
        filesDirs(new File(dir));

        int a = 1;
        if (a == 1) {
            return;
        }

        for (String filePath : filePath) {
            //String filePath = "/Users/chaoli/intellijWrkSpace/concurrenttest/src/main/resources/test/";
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            DataInput di = new DataInputStream(fis);
            ClassFile cf = new ClassFile();
            cf.read(di);
            Constant[] infos = cf.getConstantPool();

            int count = infos.length;
            for (int i = 0; i < count; i++) {
                if (infos[i] != null) {
                    //System.out.print(i);
                    //System.out.print(" = ");
                    //try {
                    //    System.out.print(infos[i].getVerbose());
                    //} catch (InvalidByteCodeException e) {
                    //    // TODO Auto-generated catch block
                    //    e.printStackTrace();
                    //}
                    //System.out.print(" = ");
                    //System.out.println(infos[i].getTagVerbose());
                    if (infos[i] instanceof ConstantLongInfo) {
                        ConstantLongInfo uInfo = (ConstantLongInfo) infos[i];
                        if (uInfo.getLong() == 1623679729980L) {
                            uInfo.setLong(System.currentTimeMillis());
                            infos[i] = uInfo;
                        }
                    }
                    //if(i == 004){
                    //    ConstantLongInfo uInfo = (ConstantLongInfo)infos[i];
                    //    uInfo.setLong(1111111111);
                    //    infos[i]=uInfo;
                    //}
                }
            }
            cf.setConstantPool(infos);
            try {
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            File f = new File(filePath);
            try {
                ClassFileWriter.writeToFile(f, cf);
            } catch (InvalidByteCodeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    //使用递归遍历文件夹及子文件夹中文件
    public static void filesDirs(File file) {
        //File对象是文件或文件夹的路径，第一层判断路径是否为空
        if (file != null) {
            //第二层路径不为空，判断是文件夹还是文件
            if (file.isDirectory()) {
                //进入这里说明为文件夹，此时需要获得当前文件夹下所有文件，包括目录
                File[] files = file.listFiles();//注意:这里只能用listFiles()，不能使用list()
                //files下的所有内容，可能是文件夹，也可能是文件，那么需要一个个去判断是文件还是文件夹，这个判断过程就是这里封装的方法
                //因此可以调用自己来判断，实现递归
                for (File flies2 : files) {
                    filesDirs(flies2);
                }
            } else {
                if (file.getName().endsWith(".class")) {
                    System.out.println(file.getAbsolutePath());
                    //filePath.add(file.getAbsolutePath());
                }
            }
        } else {
            System.out.println("文件不存在");
        }


    }
}

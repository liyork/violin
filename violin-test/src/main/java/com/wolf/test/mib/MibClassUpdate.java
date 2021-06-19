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
 * 将目录放入/Users/chaoli/Downloads/tmp/mib/目录下
 * 一劳永逸，直接修改对比时间,永不过期！
 * Created on 2021/6/18 5:23 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class MibClassUpdate {

    static HashSet<String> filePath = new HashSet<>();

    public static void main(String[] args) {
        //test();
        update();
    }

    private static void test() {
        long l = System.currentTimeMillis() - 1619539737799L;
        //if (l > 3888000000L || l < 0L) {
        //if (l > 999999999999999999L || l < 0L) {
        //
        //}
    }

    private static void update() {
        String dir = "/Users/chaoli/Downloads/tmp/mib";
        filesDirs(new File(dir));

        //int a = 1;
        //if (a == 1) {
        //    return;
        //}

        for (String filePath : filePath) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            DataInput di = new DataInputStream(fis);
            ClassFile cf = new ClassFile();
            cf.read(di);
            Constant[] infos = cf.getConstantPool();

            int count = infos.length;
            for (int i = 0; i < count; i++) {
                if (infos[i] != null) {
                    // 用于查看，注释掉
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
                        if (uInfo.getLong() == 3888000000L) {
                            //System.out.println(filePath);// 用于查看，注释掉
                            // 修改
                            uInfo.setLong(999999999999999999L);
                            infos[i] = uInfo;
                        }
                    }
                    // 按照下标修改
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
                e.printStackTrace();
            }
            File f = new File(filePath);
            try {
                ClassFileWriter.writeToFile(f, cf);
            } catch (InvalidByteCodeException | IOException e) {
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
                    //System.out.println(file.getAbsolutePath());// 临时打印
                    filePath.add(file.getAbsolutePath());// 收集
                }
            }
        } else {
            System.out.println("文件不存在");
        }
    }
}

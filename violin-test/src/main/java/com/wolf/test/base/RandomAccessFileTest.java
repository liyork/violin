package com.wolf.test.base;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Description:
 *
 * @author 李超
 * @date 2020/01/23
 */
public class RandomAccessFileTest {


    public static void main(String[] args) {

        List<String> col = new ArrayList<>();
        try {
            String pathname = RandomAccessFileTest.class.getResource("/test.txt").getPath();
            RandomAccessFile f = new RandomAccessFile(pathname, "rw");

            long len = 0L;
            long allLen = f.length();
            while (len < allLen) {
                String s = f.readLine();
                col.add(s);
                len = f.getFilePointer();
            }

        } catch (Exception err) {
            err.printStackTrace();
        }

        try {
            RandomAccessFile f = new RandomAccessFile("test1.txt", "rw");
            StringBuilder builder = new StringBuilder("\n");
            Iterator it = col.iterator();

            while (it.hasNext()) {
                builder.append(it.next()).append("\n");
            }

            f.writeUTF(builder.toString());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}

package com.wolf.test.base;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Description:
 * <br/> Created on 2016/8/9 10:37
 *
 * @author 李超()
 * @since 1.0.0
 */
public class ByteTest {

    @Test
    public void testByte() throws UnsupportedEncodingException {
        System.out.println(Charset.defaultCharset());
        System.out.println("中".getBytes().length);
        System.out.println("中".getBytes("utf-8").length);//utf-8编码后的中文占3个字节
        System.out.println("中".getBytes(Charset.forName("gbk")).length);//gbk编码后的中文占2个字节

        byte[] bytes = "中".getBytes("utf-8");
        System.out.println(bytes.length);//三个字节，1字节=8位，
        for (byte aByte : bytes) {
            //每个字节被转成二进制使用int(4字节)存储
            System.out.print(Integer.toBinaryString(aByte).toUpperCase());
            System.out.print(" ");
        }
        System.out.println();
        //16进制展示
        for (byte aByte : bytes) {
            //1个字节包含8位，每一位代表1个十进制数，每4位等于1个16进制数，结果就是2个16进制数代表8位即1个字节,
            // int代表4位，32字节，用8个16进制数代表
            System.out.print(Integer.toHexString(aByte).toUpperCase());
            System.out.print(" ");
        }


        //char占用2字节，16位
        char a = 'a';
        System.out.println(charToByte(a).length);
    }

    //1byte只能存8位，先取高8位放入b[0],再取低8位放入b[1]
    public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }
}

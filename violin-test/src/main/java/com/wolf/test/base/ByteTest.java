package com.wolf.test.base;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 * <br/> Created on 2016/8/9 10:37
 *
 * @author 李超()
 * @since 1.0.0
 */
public class ByteTest {

    // java中默认使用Unicode编码，每个字符占用两个字节，而char固定2个字节，因此可以存储1个中文，两个就不行。
    @Test
    public void testCharChinese() throws UnsupportedEncodingException {
        char a = '中';
        System.out.println(a);
    }

    @Test
    public void testByte() throws UnsupportedEncodingException {

        String ch = "abc";
        String chinese = "中";
        // 按字符计算长度
        System.out.println(ch.length() + " " + chinese.length());

        System.out.println(Charset.defaultCharset());
        System.out.println(ch.getBytes().length + " " + chinese.getBytes().length);//字符编码后字节数等于字符长度
        System.out.println(chinese.getBytes("utf-8").length);//utf-8编码后的中文占3个字节
        System.out.println(chinese.getBytes(Charset.forName("gbk")).length);//gbk编码后的中文占2个字节
    }

    @Test
    public void toHex() throws UnsupportedEncodingException {
        byte[] bytes = "中".getBytes("utf-8");
        //16进制展示
        for (byte aByte : bytes) {
            //1个字节包含8位，每3位等于1个八进制数，每4位等于1个16进制数，结果就是2个16进制数代表8位即1个字节
            System.out.print(Integer.toHexString(aByte).toUpperCase());
            System.out.print(" ");
        }
    }

    @Test
    public void toByte() throws UnsupportedEncodingException {
        byte[] bytes = "中".getBytes("utf-8");
        System.out.println(bytes.length);//三个字节，1字节=8位，
        for (byte aByte : bytes) {
            //调用toBinaryString被转换成int(4字节)存储
            System.out.print(Integer.toBinaryString(aByte).toUpperCase());
            System.out.print(" ");
        }
    }

    //char占用2字节，16位
    @Test
    public void testCharLength() {
        char a = 'a';
        System.out.println(charToByte(a).length);
    }

    //1byte只能存8位，先取高8位放入b[0],再取低8位放入b[1]
    private static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    //计算机使用补码来"表示/存储"
    @Test
    public void testBinaryCode() {
        String s1 = Integer.toBinaryString(5);
        System.out.println(s1);

        String s2 = Integer.toBinaryString(-5);
        System.out.println(s2);
    }

    // 有符号数:最高位是符号位
    // 计算机用补码，为的是减法可以用加法器执行。
    // 整型数在计算机中用补码存储的，就是下面展示的，若想求得原值(人类使用的)，需要计算(补码取反(除符号位)+1)。
    // 原值取反(除符号位)-1=补码
    @Test
    public void testByteLength() {

        int i1 = (byte) 0b11111111;
        System.out.println(i1);

        // -128比较特殊，规定-0的值代表
        int i2 = (byte) 0b10000000;
        System.out.println(i2);

        // +0则表示0
        int i3 = (byte) 0b00000000;
        System.out.println(i3);

        int i5 = (byte) 0b01111111;
        System.out.println(i5);

        // 正好得到0b10000000，即-128，一个轮回
        byte a = (byte) ((byte) 0b01111111 + 1);
        System.out.println(a);

        int i4 = (byte) 0b00000001;
        System.out.println(i4);
    }

    @Test
    public void testForceConvert() {
        short a = 128;
        //00000000 10000000
        String s2 = Integer.toBinaryString(a);
        System.out.println(s2);

        //10000000
        byte b = (byte) a;
        System.out.println(b);

        int i3 = (byte) 0b10000000;
        System.out.println(i3);
    }

    @Test
    public void testJudgeChineseCharactor() {
        judgeChineseCharactor("Hello World");
        judgeChineseCharactor("Hello 你好");
    }

    private void judgeChineseCharactor(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        if (str.getBytes().length == str.length()) {//一个中文编码后占用字节超过一个字符
            System.out.println("无中文");
        } else {
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            while (m.find()) {
                System.out.print(m.group(0) + "");
            }
        }
    }
}

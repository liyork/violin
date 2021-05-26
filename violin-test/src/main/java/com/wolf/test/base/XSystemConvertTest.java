package com.wolf.test.base;

import org.junit.Test;

/**
 * Description: 进制转换
 * Created on 2021/4/22 9:59 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class XSystemConvertTest {

    @Test
    public void testDecimal2BinaryStr() {
        System.out.println(Integer.toBinaryString(5));
        System.out.println(Integer.toBinaryString(15));
        System.out.println(Integer.toBinaryString(Integer.parseInt("D", 16)));

        // f字符的ASCII对应的数值
        System.out.println("f".toCharArray()[0]);
        System.out.println('f');
        // f字符对应的16进制的数值
        System.out.println(Integer.parseInt("f", 16));

        System.out.println(hexCharToInt('F'));
        System.out.println(Integer.toBinaryString('f'));
    }

    // 按照位数将16进制字符转成对应的数值
    private static int hexCharToInt(char c) {
        return "0123456789ABCDEF".indexOf(c);
    }

    @Test
    public void testString2Int() {
        // 底层调用parseInt+valueOf，valueOf是缓存或new
        System.out.println(Integer.valueOf("111"));
        System.out.println(Integer.parseInt("111"));
        System.out.println(Integer.parseInt("111", 10));// 字符串中是10进制的数
        System.out.println(Integer.parseInt("f", 16));// 字符串中是16进制的数
    }

    @Test
    public void testBinary2Int() {
        System.out.println(Integer.valueOf("0101", 2));
    }

    // 0 1 2 3 4 5 6 7 8 9 a b c d e f
    @Test
    public void testInt2Hex() {
        System.out.println(Integer.toHexString(5));
        System.out.println(Integer.toHexString(15));
        System.out.println(Integer.toHexString(14));
    }
}

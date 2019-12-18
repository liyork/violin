package com.wolf.test.base;

import org.junit.Test;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/12/07
 */
public class BItTest {

    // >>有符号右移，若运算数为正，在高位补0，若为负载高位补1.
    // >>>运算数正负，都在高位补0
    // 左移仅在低位补0
    @Test
    public void moveBit() {
        int i = -4;
        System.out.println(Integer.toBinaryString(i));
        System.out.println(i);

        i >>= 1;
        System.out.println(Integer.toBinaryString(i));//11111111111111111111111111111110
        System.out.println(i);

        i = -4;
        i >>>= 1;
        System.out.println(Integer.toBinaryString(i));//01111111111111111111111111111110
        System.out.println(i);
    }

    @Test
    public void moveBitShort() {
        short j = -4;// short只占用2byte
        System.out.println(Integer.toBinaryString(j));//调用时转换成int(4byte)，高位补1
        System.out.println(j);
        j >>>= 1;// 移位时先转换为int，无符号移位-高位补0，再赋值给short类型的j，只会取低位2字节。
        System.out.println(Integer.toBinaryString(j));
        System.out.println(j);
    }

    // int变量占用4byte(32bit)，移动范围0-31，当右移超过32bit时，移位运算没有任何意义。为保证移动位数的有效性，以使右移
    // 的位数不超过32bit，采用取余的操作，即a>>n等价于a>>(n%32)
    @Test
    public void moveBitOver() {
        int i = 5;
        System.out.println(i >> 2);
        System.out.println(i >> 32);
    }

    // 左乘右除，移动几位就是2^n
    // CPU直接支持位运算，比乘法运算的效率高。
    @Test
    public void replaceMulti() {
        System.out.println(3 << 4);
    }
}

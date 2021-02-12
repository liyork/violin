package com.wolf.test.writeexam;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

/**
 * Description:
 * 大端模式(BIG-ENDIAN)
 * 数据的高位，保存在内存的低地址中。数据的低位，保存在内存的高地址
 * 地址由小向大增加，而数据从高位往低位放
 * <p>
 * 为什么会有大小端模式之分呢？
 * 因为在计算机系统中，我们是以字节为单位的，每个地址单元都对应着一个字节，一个字节为 8bit
 * 但是在C语言中除了8bit的char之外，还有16bit的short型等
 * 对于位数大于 8位的处理器，例如16位或者32位的处理器，由于寄存器宽度大于一个字节，那么必然存在着一个如何将多个字节安排的问题
 * 因此就导致了大端存储模式和小端存储模式
 * <p>
 * 例如一个16bit的short型x，在内存中的地址为0x0010，x的值为0x1122，那么0x11为高字节，0x22为低字节。
 * 对于大端模式，就将0x11放在低地址中，即0x0010中，0x22放在高地址中，即0x0011中。(地址从小到大涨)
 * <p>
 * 在java网络开发中通常涉及到字节序的数据类型是多字节数据类型：int，short，long型等，单字节数据byte没有影响。由于java采用的字节序同网络协议采用的字节序是一样的，它们都是选择的人们通常更容易理解的大端模式(big endian)
 * <br/> Created on 2017/4/22 6:49
 *
 * @author 李超
 * @since 1.0.0
 */
public class ByteOrderTest {

    public static void main(String[] args) throws IOException {
        // mac-cpu字节序,小端
        System.out.println(ByteOrder.nativeOrder());

        //内存中以字节为单位存放，前半部分代表高位，后半部分代表低位(内存是从小到大涨,哪端决定将多个字节如何存放在内存相应位置)
        // 下标0在内存中地址小，下标1在内存中紧挨着下标0
        byte[] arr = new byte[4];
        arr[0] = 0x56;
        arr[1] = 0x34;
        arr[2] = 0x78;
        arr[3] = 0x12;
//		System.out.println(Arrays.toString(arr));

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arr);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        //java内存中采用大端方式存储字节，高位字节序放在低内存地址上
        //内存地址   低地址----->高地址
        System.out.println(Integer.toHexString(dataInputStream.readInt()));// int=4byte
    }
}

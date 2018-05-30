package com.wolf.test.writeexam;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

/**
 * Description:
 * 注意内存中字节的高位、低位
 * 内存地址的高地址、低地址
 * <br/> Created on 2017/4/22 6:49
 *
 * @author 李超
 * @since 1.0.0
 */
public class ByteOrderTest {

	public static void main(String[] args) throws IOException {
		//cpu字节序,小端
		System.out.println(ByteOrder.nativeOrder());

		//内存以字节为单位存放的，前半部分代表高位，后半部分代表低位
		byte[] arr = new byte[4];
		arr[0] = 0x56;
		arr[1] = 0x34;
		arr[2] = 0x78;
		arr[3] = 0x12;

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arr);
		DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
		//java内存中采用大端方式存储字节，高位放在低地址上
		//内存地址   低地址----->高地址
		System.out.println(Integer.toHexString(dataInputStream.readInt()));
	}
}

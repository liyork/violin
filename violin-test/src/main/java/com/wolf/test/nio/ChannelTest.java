package com.wolf.test.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p> Description:
 * <p/>
 * Date: 2015/10/26
 * Time: 8:38
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ChannelTest {

	public static void main(String[] args) throws Exception {
//		testRead();
		testWrite();
	}


	public static void testRead() throws Exception {
		FileInputStream fin = new FileInputStream("c:\\test.txt");

		// 获取通道
		FileChannel fc = fin.getChannel();

		// 创建缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);

		// 读取数据到缓冲区
		fc.read(buffer);

		buffer.flip();

		while (buffer.remaining() > 0) {
			byte b = buffer.get();
			System.out.print(((char) b));
		}

		fin.close();
	}

	public static void testWrite() throws IOException {
		final byte message[] = {83, 111, 109, 101, 32,
				98, 121, 116, 101, 115};

		FileOutputStream fout = new FileOutputStream("c:\\test.txt");

		FileChannel fc = fout.getChannel();

		ByteBuffer buffer = ByteBuffer.allocate(1024);

		buffer.put(message);

		//需要重置pos
		buffer.flip();

		fc.write(buffer);

		fout.close();
	}
}

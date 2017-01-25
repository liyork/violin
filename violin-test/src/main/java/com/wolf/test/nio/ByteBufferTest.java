package com.wolf.test.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Locale;

/**
 * <p> Description:
 * <p/>
 * Date: 2015/10/26
 * Time: 8:31
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ByteBufferTest {

	@Test
	public void testSimpleUsage() {
		// 分配新的int缓冲区，参数为缓冲区容量
		// 新缓冲区的当前位置pos(指向将要放入元素位置，即下一元素位置)将为零，其界限lim(限制位置)将为其容量capacity。
		// 它将具有一个底层实现数组，其数组偏移量offset将为零。
		IntBuffer buffer = IntBuffer.allocate(8);

		for (int i = 0; i < buffer.capacity(); ++i) {
			int j = 2 * (i + 1);
			// 将给定整数写入此缓冲区的当前位置，当前位置递增
			buffer.put(j);//pos++
		}

		// 重设此缓冲区，将限制(lim)设置为当前位置，然后将当前位置设置为0,目的是读取或者重新放值
		buffer.flip();//limit=pos,pos=0

		// 查看在当前位置和限制位置之间是否有元素
		while (buffer.hasRemaining()) {
			// 读取此缓冲区当前位置的整数，然后当前位置递增
			int j = buffer.get();
			System.out.print(j + "  ");
		}
	}


	@Test
	public void testInternalPrinciple() throws Exception {

		FileInputStream fin = new FileInputStream("d:\\test.txt");
		FileChannel fc = fin.getChannel();

		ByteBuffer buffer = ByteBuffer.allocate(10);
		output("初始化", buffer);

		fc.read(buffer);
		output("调用read()", buffer);

		buffer.flip();
		output("调用flip()", buffer);

		while (buffer.remaining() > 0) {
			byte b = buffer.get();
			System.out.print(((char) b));
		}
		output("调用get()", buffer);

		buffer.clear();
		output("调用clear()", buffer);

		fin.close();
	}


	private static void output(String step, Buffer buffer) {
		System.out.println(step + " : ");
		System.out.print("position: " + buffer.position() + ", ");
		System.out.print("limit: " + buffer.limit() + ", ");
		System.out.println("capacity: " + buffer.capacity());
		System.out.println();
	}


	@Test
	public void testAllocate() throws Exception {

		// 分配指定大小的缓冲区
		ByteBuffer buffer1 = ByteBuffer.allocate(10);
		// 包装一个现有的数组
		byte array[] = new byte[10];
		ByteBuffer buffer2 = ByteBuffer.wrap(array);

	}

	/**
	 * 子缓冲区与主共享一个数组，使用offset控制元素，
	 * 使用的get( i )或put( i, b )不会改变pos
	 * 使用的get()或put(b)会改变pos
	 *
	 * @throws Exception
	 */
	@Test
	public void testSubBuffer() throws Exception {
		ByteBuffer parent = ByteBuffer.allocate(10);

		// 缓冲区中的数据0-9
		for (int i = 0; i < parent.capacity(); ++i) {
			parent.put((byte) i);
		}

		// 创建子缓冲区
		parent.position(3);
		parent.limit(7);
		ByteBuffer children = parent.slice();

		// 改变子缓冲区的内容
		for (int i = 0; i < children.capacity(); ++i) {
			byte b = children.get(i);
			b *= 10;
			children.put(i, b);
		}

		parent.position(0);
		parent.limit(parent.capacity());

		while (parent.remaining() > 0) {
			System.out.println(parent.get());
		}
	}

	//创建一个HeapByteBufferR对象，仅共享hb
	@Test
	public void testReadOnlyBuffer() throws Exception {
		ByteBuffer buffer = ByteBuffer.allocate(10);

		// 缓冲区中的数据0-9
		for (int i = 0; i < buffer.capacity(); ++i) {
			buffer.put((byte) i);
		}

		// 创建只读缓冲区
		ByteBuffer readonly = buffer.asReadOnlyBuffer();

		// 改变原缓冲区的内容
		for (int i = 0; i < buffer.capacity(); ++i) {
			byte b = buffer.get(i);
			b *= 10;
			buffer.put(i, b);
		}

		readonly.position(0);
		readonly.limit(buffer.capacity());

		// 只读缓冲区的内容也随之改变
		while (readonly.remaining() > 0) {
			System.out.println(readonly.get());
		}

		//报错
		readonly.put(1, (byte) 2);
	}

	@Test
	public void testDirectBuffer() throws Exception {

		String infile = "d:\\test.txt";
		FileInputStream fin = new FileInputStream(infile);
		FileChannel fcin = fin.getChannel();

		String outfile = String.format("d:\\testcopy.txt");
		System.out.println(Locale.getDefault());//zh_CN

		FileOutputStream fout = new FileOutputStream(outfile);
		FileChannel fcout = fout.getChannel();

		// 使用allocateDirect，而不是allocate
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

		while (true) {
			buffer.clear();

			int r = fcin.read(buffer);

			if (r == -1) {
				break;
			}

			buffer.flip();

			fcout.write(buffer);
		}
	}


	@Test
	public void  testMapBuffer() throws Exception {
		RandomAccessFile raf = new RandomAccessFile("d:\\test.txt", "rw");
		FileChannel fc = raf.getChannel();

		MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE,0, 1024);

		mbb.put(0, (byte) 97);
		mbb.put(1023, (byte) 122);

		raf.close();
	}

}

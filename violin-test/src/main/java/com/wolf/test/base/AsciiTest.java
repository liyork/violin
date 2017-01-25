package com.wolf.test.base;

import org.junit.Test;

import java.nio.charset.Charset;

/**
 * <p> Description:
 * 工具:
 * native2ascii 回车 中文 ctrl+c结束
 * native2ascii -reverse \u4e2d\u6587
 * native2ascii D:\a.txt D:\b.txt
 * native2ascii -reverse D:\a.txt D:\b.txt
 * native2ascii -encoding utf8 D:\a.txt D:\b.txt  前提是a文件要设定为utf-8格式的文本
 * <p/>
 * Date: 2016/8/12
 * Time: 0:00
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class AsciiTest {

	public static void main(String[] args) {
		System.out.println('a');
		//一个byte占8位，一个字符一个字节，强转没事
		System.out.println((byte) 'a');
		//一个int占32位
		System.out.println((int) 'a');
		//97对应的char是a
		System.out.println((char) 97);

		System.out.println(Charset.defaultCharset().name());//utf-8
		System.out.println("中".getBytes().length);

		System.out.println(sumCharAscii("一"));

		System.out.println(ascii2Char());

		char[] x = "新年快乐".toCharArray();
		for (char c : x) {
			System.out.println((int) c);
		}

		System.out.println(System.getProperty("file.encoding"));

	}

	private static char ascii2Char() {
		return (char) Integer.parseInt("22307");
	}

	private static int sumCharAscii(String str) {
		byte[] byteChars = str.getBytes();
		int sum = 0;
		for (byte byteChar : byteChars) {
			sum += byteChar;
		}
		return sum;
	}

	@Test
	public void testEncodeAndDecode() throws Exception {
		String s = "I am 君山";//7个字符
//		String s = "淘！我喜欢！a";//7个字符
		char[] chars = s.toCharArray();//unicode表示,英文一个字节，中文两个字节
		System.out.println("unicode");
		for (char aChar : chars) {
			System.out.print(Integer.toHexString(aChar));
			System.out.print(" ");
		}

		System.out.println();
		System.out.println("ISO-8859-1");
		byte[] bytes1 = s.getBytes("ISO-8859-1");//一个字符用一个字节存储
		for (byte aByte : bytes1) {
			System.out.print(Integer.toHexString(aByte));
			System.out.print(" ");
		}

		System.out.println();
		System.out.println("GB2312");
		byte[] bytes2 = s.getBytes("GB2312");//一个英文字符用一个字节存储，中文字符用两个字节
		for (byte aByte : bytes2) {
			System.out.print(Integer.toHexString(aByte));
			System.out.print(" ");
		}

		System.out.println();
		System.out.println("GBK");
		byte[] bytes3 = s.getBytes("GBK");//同GB2312，比GB2312码表数多
		for (byte aByte : bytes3) {
			System.out.print(Integer.toHexString(aByte));
			System.out.print(" ");
		}

		System.out.println();
		System.out.println("UTF-16");
		byte[] bytes4 = s.getBytes("UTF-16");//一个字符统一用两个字节存储，最前面表明是高位在前还是低位在前
		for (byte aByte : bytes4) {
			System.out.print(Integer.toHexString(aByte));
			System.out.print(" ");
		}

		System.out.println();
		System.out.println("UTF-8");
		byte[] bytes5 = s.getBytes("UTF-8");//对单字节范围内字符仍然用一个字节表示，对汉字采用三个字节表示
		for (byte aByte : bytes5) {
			System.out.print(Integer.toHexString(aByte));
			System.out.print(" ");
		}
	}
}

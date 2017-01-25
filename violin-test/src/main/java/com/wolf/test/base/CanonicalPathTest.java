package com.wolf.test.base;

import java.io.File;
import java.io.IOException;

/**
 * <b>功能</b>
 * getPath()返回的是File构造方法里的路径，是什么就是什么，不增不减
 * getAbsolutePath()返回的其实是user.dir+"\"+getPath()的内容
 * getCanonicalPath()返回的就是标准的将符号完全解析的路径,如果是相对则从user.dir开始，如果是绝对则从参数开始
 *
 * @author 李超
 * @Date 2016/5/2
 */
public class CanonicalPathTest {

	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir"));
		System.out.println("path1...");
		File classPath1 = new File("..\\src\\testCanonicalPath.txt");
		System.out.println(classPath1.getPath());
		System.out.println(classPath1.getAbsolutePath());
		System.out.println(classPath1.getCanonicalPath());

		System.out.println("path2...");
		File classPath2 = new File(".\\testCanonicalPath.txt");
		System.out.println(classPath2.getPath());
		System.out.println(classPath2.getAbsolutePath());
		System.out.println(classPath2.getCanonicalPath());

		System.out.println("path3...");
		File classPath3 = new File("D:\\intelliJWorkSpace\\xxCOMPARNY\\src\\testCanonicalPath.txt");
		System.out.println(classPath3.getPath());
		System.out.println(classPath3.getAbsolutePath());
		System.out.println(classPath3.getCanonicalPath());
	}
}

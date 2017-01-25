package com.wolf.structural.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/6
 * Time: 9:07
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Client {

	public static void main(String[] args) throws FileNotFoundException {
		Compiler compiler = new Compiler();
		compiler.compile(new FileInputStream(new File("a.txt")));
	}
}

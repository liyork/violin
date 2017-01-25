package com.wolf.test.base;

import java.util.UUID;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/4/11
 * Time: 17:33
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class UUIDTest {

	public static void main(String[] args) {
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid.toString());

		System.out.println(System.getProperty("line.separator").equals("\r\n"));
	}
}

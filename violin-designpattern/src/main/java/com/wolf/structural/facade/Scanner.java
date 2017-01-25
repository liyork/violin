package com.wolf.structural.facade;

import java.io.InputStream;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/6
 * Time: 8:43
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Scanner {

	public Token scan(InputStream inputStream) {
		return new Token();
	}
}

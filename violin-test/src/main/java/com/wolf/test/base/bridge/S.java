package com.wolf.test.base.bridge;

/**
 * Description:
 * <br/> Created on 2016/10/27 14:36
 *
 * @author 李超()
 * @since 1.0.0
 */
class S extends P<String> {

	@Override
	public String test(String t) {
		return t;
	}
}
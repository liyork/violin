package com.wolf.test.generics.entity;

import java.io.Serializable;

/**
 * <p> Description:
 * <p/>
 * Date: 2015/7/21
 * Time: 8:45
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Children extends Person implements Serializable {

	public Children(int id, String name) {
		super(id, name);
	}
}

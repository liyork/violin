package com.wolf.test.entity;

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
public class Person implements Serializable {

	private int id;
	private String name;

	public Person() {
	}

	public Person(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

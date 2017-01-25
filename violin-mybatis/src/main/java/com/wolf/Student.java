package com.wolf;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2015/6/18
 */
public class Student {

	//定义属性，和student表中的字段对应
	private int id;            //id===>s_id
	private String name;    //name===>s_name
	private int age;

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

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + "]";
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAge() {
		return age;
	}
}

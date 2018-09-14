package com.wolf.test.json;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/29
 * Time: 15:16
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class UserInfo implements Serializable,UserInfoInterface {
	@JsonIgnore//这个是fasterxml的。。
	private String name;

	@JSONField(serialize = false)//这是ali的。。
	private int age;

	private int age1;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAge() {
		return age;
	}

	public int getAge1() {
		return age1;
	}

	public void setAge1(int age1) {
		this.age1 = age1;
	}

	//如果有这个，json.tostring时会查找bean中的getxx字段，然后对这个value进行tojson。。。那么又回来了。。
//	public UserInfo getObject(){
//		return new UserInfo();
//	}

}
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

import java.io.Serializable;

public class UserInfo implements Serializable,UserInfoInterface {
	private String name;
	private int age;

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

	//如果有这个，json.tostring时会查找bean中的getxx字段，然后对这个value进行tojson。。。那么又回来了。。
//	public UserInfo getObject(){
//		return new UserInfo();
//	}

}
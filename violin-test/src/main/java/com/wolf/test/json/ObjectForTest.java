package com.wolf.test.json;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/8/2
 * Time: 9:00
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ObjectForTest {

	private String name;
	private int age;
	private Integer count;
	private List<String> list1 = new ArrayList<String>();
	private List<String> list2;
	private EnumForTest enumForTest = EnumForTest.A ;
	private transient String address;
	private Boolean isMan;

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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<String> getList1() {
		return list1;
	}

	public void setList1(List<String> list1) {
		this.list1 = list1;
	}

	public List<String> getList2() {
		return list2;
	}

	public void setList2(List<String> list2) {
		this.list2 = list2;
	}

	public EnumForTest getEnumForTest() {
		return enumForTest;
	}

	public void setEnumForTest(EnumForTest enumForTest) {
		this.enumForTest = enumForTest;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getIsMan() {
		return isMan;
	}

	public void setIsMan(Boolean isMan) {
		this.isMan = isMan;
	}

	private enum EnumForTest{
		A;

		@Override
		public String toString() {
			return this.name()+"xx";
		}
	}

	public static void main(String[] args) {
		EnumForTest a = EnumForTest.A;
		System.out.println(a.toString());
	}

}



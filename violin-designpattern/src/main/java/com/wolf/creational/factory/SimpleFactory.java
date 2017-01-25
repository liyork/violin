package com.wolf.creational.factory;

import com.wolf.utils.ClassSearchUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/14
 * Time: 9:27
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class SimpleFactory {

	private static Map<String,Class> relation = new HashMap<String, Class>();

	public static void register(String productName,Class productClass) {
		relation.put(productName, productClass);
	}


	//1==================
	/**
	 * 简单直接的实现，使用参数判断返回哪个产品。
	 * 如果有新的类型加入：1.添加新类型。2.需要修改这里。3.客户端使用新类型
	 * 修改：如果内部有变化，只要约定的名称不变，客户不需要改变
	 * @param productName
	 * @return
	 */
	public static Product getInstance1(String productName) {
		Product product = null;
		if (productName.equals("concreteProduct1")) {
			product = new ConcreteProduct1();
		}else if(productName.equals("concreteProduct2")){
			product = new ConcreteProduct2();
		}

		return product;
	}


	//2==================
	/**
	 * 使用反射获取已经注册到的产品，调用对应产品的创建方法。
	 * 新增：如果有新的类型加入：1.添加新类型。2.客户端使用新类型
	 * 修改：如果内部有变化，只要约定的名称不变，客户不需要改变
	 * @param productName
	 * @return
	 */
	public static Product getInstance2(String productName){
		Class productClass = relation.get(productName);
		try {
			return ((Product)productClass.newInstance()).getProduct();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}


	//3==================
	/**
	 * 客户直接传入类全名，这里直接使用加载
	 * 修改：暴露给客户信息太多，以后如果修改类名或路径，客户也得修改。
	 * @param productFullName
	 * @return
	 */
	public static Product getInstance3(String productFullName){
		try {
			return ((Product)Class.forName(productFullName).newInstance()).getProduct();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}


	//4==================,先静态加载指定包下的所有类(触发他们的静态注册方法，注册到当前工厂)
	static {
		List<String> classes = ClassSearchUtils.getClassesFromPackage("com.car.designpattern.creational.factory");
		for (String className : classes) {
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 使用反射获取已经注册到的产品，调用对应产品的创建方法。
	 * 新增：如果有新的类型加入：1.添加新类型。2.客户端使用新类型
	 * 修改：如果内部有变化，只要约定的名称不变，客户不需要改变
	 * @param productName
	 * @return
	 */
	public static Product getInstance4(String productName){
		Class productClass = relation.get(productName);
		try {
			return ((Product)productClass.newInstance()).getProduct();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}
}

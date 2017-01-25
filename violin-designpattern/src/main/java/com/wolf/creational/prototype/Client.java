package com.wolf.creational.prototype;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/3
 */
public class Client {

	public static void main(String[] args) {
		PrototypeManger prototypeManger = new PrototypeManger();
		ConcreteClass1 concreteClass1Pro = new ConcreteClass1();
		String class1Key = "concreteClass1Pro";
		prototypeManger.register(class1Key,concreteClass1Pro);
		ConcreteClass2 concreteClass2Pro = new ConcreteClass2();
		String class2Key = "concreteClass2Pro";
		prototypeManger.register(class2Key,concreteClass2Pro);

		ConcreteClass1 concreteClass1 = prototypeManger.cloneObject(class1Key,ConcreteClass1.class);
		System.out.println(concreteClass1Pro == concreteClass1);
		ConcreteClass2 concreteClass2 = prototypeManger.cloneObject(class2Key,ConcreteClass2.class);
		System.out.println(concreteClass2Pro == concreteClass2);
	}
}

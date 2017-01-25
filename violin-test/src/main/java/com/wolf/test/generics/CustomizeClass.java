package com.wolf.test.generics;

import com.wolf.test.generics.entity.Children;
import com.wolf.test.generics.entity.Person;

import java.util.Comparator;

/**
 * <p> Description:泛型类
 * 定义在类上泛型，在方法上的泛型
 * 泛型的本质是参数化类型，也就是说所操作的数据类型被指定为一个参数
 * 提高重用性，当CustomizeClass放入person或是children都可以，不用定义两个类
 * 编译之前检查的
 * note:泛型类，只能用于非静态方法
 * 泛型方法如果单独定义类型则用于静态方法,如果用类的泛型则是实例方法
 * <p/>
 * Date: 2015/7/21
 * Time: 8:44
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class CustomizeClass<T> extends BasicGenericClass<Person> implements Comparator<Children> {

	private T entity;

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public void setEntity1(Person entity) {
	}

	public T getEntity() {
		return entity;
	}

	//泛型不能应用都在静态方法或字段上(泛型类中的泛型参数的实例化是在定义对象的时候指定的)
//	public static T getxx(){
//	}

	//类型擦除后变成equals(Object object)与object的equals冲突
//	public void equals(T t){
//	}

	@Override
	public int compare(Children o1, Children o2) {
		return o1.getId() - o2.getId();
	}
}







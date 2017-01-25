package com.wolf.creational.prototype;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>功能</b>
 * The Prototype pattern can greatly reduce the number of classes a system needs
 * we should consider the new class whether is-a class from old class,or is a object from old class.
 * I think this pattern should be used when we need create object which is similar to old object and
 * has very few difference from old object.
 *
 * @author 李超
 * @Date 2016/7/3
 */
public class PrototypeManger {

	private Map<String,CloneInterface> map = new HashMap<String, CloneInterface>();

	public void register(String key,CloneInterface value) {
		map.put(key, value);
	}

	public <T> T cloneObject(String key,Class<T> targetClass) {
		T result = null;
		try {
			CloneInterface clone = (CloneInterface)map.get(key).clone();
			//todo 也可考虑用户自己初始化对象，或者让用户传入参数和方法，这里通过反射初始化对象。
			//TODO 也要考虑深拷贝里面对象的初始化。
			clone.initial();
			result = (T) clone;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return result;
	}
}

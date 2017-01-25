package com.wolf.utils;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/30
 * Time: 14:16
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ObjectCopyUtils {

	/**
	 * @param targetClass
	 * @param sourceObject
	 * @param <T>
	 * @return 注：谨慎使用targetClass作为接口，而不是类
	 */
	public static <T> T copy(Class<T> targetClass, Object sourceObject) {
		String jsonParam = JSON.toJSONString(sourceObject);
		return JSON.parseObject(jsonParam, targetClass);
	}

	//将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
	public static <T> T copyByStream(Class<T> targetClass, Object sourceObject) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(sourceObject);
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);
		return (T) ois.readObject();
	}
}

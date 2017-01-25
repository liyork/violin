package com.wolf.test.base.serialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.SerializerFactory;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * <p> Description: 序列化测试
 * 不会调用任何对象的构造方法
 * 使用默认机制，在序列化对象时，不仅会序列化当前对象本身，还会对该对象引用的其它对象也进行序列化
 * <p/>
 * Date: 2015/7/20
 * Time: 9:02
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class SerializeTest {


	public static void main(String[] args) throws Exception {
		testJavaSerialize();
//		testHessianSerialize();

//		testIfSingletonJavaSerialize();
	}


	private static void testIfSingletonJavaSerialize() throws IOException, ClassNotFoundException {
		System.out.println("testJavaSerialize ....");
		SerializeObject serializeObject = SerializeObject.getInstance();

		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
		objectOutputStream.writeObject(serializeObject);

		byte[] bytes = byteOutputStream.toByteArray();

		byteOutputStream.flush();
		byteOutputStream.close();
		objectOutputStream.close();


		ByteInputStream byteInputStream = new ByteInputStream(bytes,bytes.length);
		ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
		Object o = objectInputStream.readObject();
		System.out.println(o);
		//这是错误的，可能连接符【+】的级别比【==】高，所以"if === ? "+serializeObject先被转化成string然后再用【==】进行比较
//		System.out.println("if === ? "+serializeObject == o);
		System.out.println(serializeObject == o);

		byteInputStream.close();
		objectInputStream.close();
	}

	private static void testJavaSerialize() throws IOException, ClassNotFoundException {
		System.out.println("testJavaSerialize ....");
		SerializeObject serializeObject = createObject();

		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
		objectOutputStream.writeObject(serializeObject);

		//设定类变量
		SerializeObject.count = 5;

		byte[] bytes = byteOutputStream.toByteArray();

		byteOutputStream.flush();
		byteOutputStream.close();
		objectOutputStream.close();


		ByteInputStream byteInputStream = new ByteInputStream(bytes,bytes.length);
		ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
		Object o = objectInputStream.readObject();
		System.out.println(o);
		System.out.println("if === ? "+serializeObject == o);

		byteInputStream.close();
		objectInputStream.close();
	}

	private static void testHessianSerialize() throws IOException, ClassNotFoundException {
		System.out.println("testHessianSerialize ....");
		SerializeObject serializeObject = createObject();
		//序列化工厂
		SerializerFactory serializerFactory = new SerializerFactory();

		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		HessianOutput hessianOutput = new HessianOutput(byteOutputStream);
		hessianOutput.setSerializerFactory(serializerFactory);
		hessianOutput.writeObject(serializeObject);

		byte[] bytes = byteOutputStream.toByteArray();

		byteOutputStream.flush();
		byteOutputStream.close();
		hessianOutput.close();

		ByteInputStream byteInputStream = new ByteInputStream(bytes,bytes.length);
		HessianInput hessianInput = new HessianInput(byteInputStream);
		hessianInput.setSerializerFactory(serializerFactory);
		Object o = hessianInput.readObject();
		System.out.println(o);

		byteInputStream.close();
		hessianInput.close();
	}


	private static SerializeObject createObject() {
		SerializeObject serializeObject = new SerializeObject();
		serializeObject.setId(1);
		serializeObject.setOrderNo("xxxx");
		serializeObject.setPrice(2.45f);
		serializeObject.setCarNo("110010");
		return serializeObject;
	}

}

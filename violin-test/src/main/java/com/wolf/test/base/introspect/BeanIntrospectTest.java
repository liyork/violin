package com.wolf.test.base.introspect;

import java.beans.*;
import java.lang.reflect.Method;

/**
 * Description:
 *    "Reflection" refers to the low-level API with which we can discover "what are the methods of this class",
 *    Introspection" is a higher-level concept; it generally uses the reflection API to know about a class.
 *    Or we can say that Introspection is concept of investigating about any Object at runtime for its type/class /method
 *    	details and this capability is provided by the reflection API.
 *    Type introspection is not reflection, however; reflection takes these core principles of type introspection and allows
 *    	us to do some really cool, powerful, and sometimes scary things with our code.
 *    If type introspection allows you to inspect an object’s attributes at runtime, then reflection is what allows you to manipulate those attributes at runtime.
 *	  type introspection is just inspecting an object’s attributes, and reflection is the actual manipulating or invoking of an object’s attributes or functions
 *	  In fact introspection feature uses the low level reflection to check the objects internal attributes.
 * <br/> Created on 2016/11/8 17:20
 *
 * @author 李超()
 * @since 1.0.0
 */
public class BeanIntrospectTest {


	public static void main(String[] args) throws Exception {
		//实例化一个Bean
		TestBean beanObj = new TestBean();
		//依据Bean产生一个相关的BeanInfo类
		BeanInfo beanInfo = Introspector.getBeanInfo(beanObj.getClass(), beanObj.getClass().getSuperclass());
		testIntrospectMethod(beanInfo);
		testIntrospectProperties(beanInfo);
		testIntrospectEvent(beanInfo);

		// 通过类名来构造一个类的实例
		Class cls_str1 = Class.forName( "java.lang.String" );
		Class cls_str2 = Class.forName( "java.lang.String" );
		System.out.println(cls_str1==cls_str2);
	}

	/*
	* BeanInfo.getMethodDescriptors()
    * 用于获取该Bean中的所有允许公开的成员方法，以MethodDescriptor数组的形式返回
    *
    * MethodDescriptor类
    * 用于记载一个成员方法的所有信息
    * MethodDescriptor.getName()
    * 获得该方法的方法名字
    * MethodDescriptor.getMethod()
    * 获得该方法的方法对象（Method类）
    *
    * Method类
    * 记载一个具体的的方法的所有信息
    * Method.getParameterTypes()
    * 获得该方法所用到的所有参数，以Class数组的形式返回
    *
    * Class..getName()
    * 获得该类型的名字
    */
	private static BeanInfo testIntrospectMethod(BeanInfo beanInfo) throws IntrospectionException {
		//定义一个用于显示的字符串
		String output = "内省成员方法： ";
		MethodDescriptor[] methodDescArray = beanInfo.getMethodDescriptors();
		for (MethodDescriptor methodDesc : methodDescArray) {
			//获得一个成员方法描述器所代表的方法的名字
			String methodName = methodDesc.getName();

			String methodParams = "";
			//获得该方法对象
			Method methodObj = methodDesc.getMethod();
			//通过方法对象获得该方法的所有参数，以Class数组的形式返回
			Class[] parameters = methodObj.getParameterTypes();
			if (parameters.length > 0) {
				for (Class parameter : parameters) {
					methodParams = parameter.getName() + "," + methodParams;
				}
				methodParams = methodParams.substring(0, methodParams.lastIndexOf(","));
			}
			output += methodName + "(" + methodParams + ") ";
		}
		System.out.println(output);


		return beanInfo;
	}

	/*
	* BeanInfo.getPropertyDescriptors()
    * 用于获取该Bean中的所有允许公开的成员属性，以PropertyDescriptor数组的形式返回
    *
    * PropertyDescriptor类
    * 用于描述一个成员属性
    *
    * PropertyDescriptor.getName()
    * 获得该属性的名字
    *
    * PropertyDescriptor.getPropertyType()
    * 获得该属性的数据类型，以Class的形式给出
    *
    */
	private static void testIntrospectProperties(BeanInfo beanInfo) {
		String output = "内省成员属性： ";
		PropertyDescriptor[] mPropertyArray = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor aMPropertyArray : mPropertyArray) {
			String propertyName = aMPropertyArray.getName();
			Class propertyType = aMPropertyArray.getPropertyType();
			output += propertyName + " ( " + propertyType.getName() + " ) ";
		}
		System.out.println(output);
	}

	/*
	* BeanInfo.getEventSetDescriptors()
    * 用于获取该Bean中的所有允许公开的成员事件，以EventSetDescriptor数组的形式返回
    *
    * EventSetDescriptor类
    * 用于描述一个成员事件
    *
    * EventSetDescriptor.getName()
    * 获得该事件的名字
    *
    * EventSetDescriptor.getListenerType()
    * 获得该事件所依赖的事件监听器，以Class的形式给出
    *
    */
	private static void testIntrospectEvent(BeanInfo beanInfo) {
		String output = "内省绑定事件： ";
		EventSetDescriptor[] mEventArray = beanInfo.getEventSetDescriptors();
		for (EventSetDescriptor aMEventArray : mEventArray) {
			String EventName = aMEventArray.getName();
			Class listenerType = aMEventArray.getListenerType();
			output += EventName + "(" + listenerType.getName() + ") ";
		}
		System.out.println(output);
	}
}

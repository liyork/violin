package com.wolf.test.base.reflect;

import com.wolf.test.annotation.BusinessService;
import com.wolf.test.annotation.CacheResult;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Description:
 * <br/> Created on 2016/10/27 15:08
 *
 * @author 李超()
 * @since 1.0.0
 */
public class ReflectTest {

	public static void main(String[] args) throws NoSuchMethodException {
		Method test1 = C.class.getMethod("test", null);
		System.out.println(test1);
		Method test2 = B.class.getMethod("test", null);
		System.out.println(test2);
		System.out.println(test1.equals(test2));
		Method test3 = A.class.getMethod("test", null);
		System.out.println(test3);
		System.out.println(test1.equals(test3));

		Method test4 = B.class.getMethod("test1", null);
		//protected不能被getDeclaringClass获取到
		//System.out.println(test4.getDeclaringClass());
	}

	@Test
	public void testAnnotationInstance() throws NoSuchMethodException {
		Method getCityName = BusinessService.class.getMethod("getCityName", null);
		Annotation[] annotations = getCityName.getAnnotations();
		for (Annotation annotation : annotations) {
			System.out.println(CacheResult.class.isInstance(annotation));
			System.out.println(annotation instanceof CacheResult);
		}
	}
}

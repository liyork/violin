package com.wolf.test.spring.beanwrapper;

import org.junit.Test;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <br/> Created on 2016/11/9 13:50
 *
 * @author 李超()
 * @since 1.0.0
 */
public class BeanWrapperTest {

	@Test
	public void testBeanWrapperSourceCode() throws InvocationTargetException, IllegalAccessException {
		Company company = new Company();
		BeanWrapperImpl companyBeanWrapper = new BeanWrapperImpl(company);

		PropertyDescriptor companyPD = companyBeanWrapper.getPropertyDescriptor("companyName");
		String name = companyPD.getName();
		System.out.println("name==>"+name);
		Method writeMethod = companyPD.getWriteMethod();
		System.out.println("writeMethod==>"+writeMethod.getName());
		writeMethod.invoke(company, "123");
		Method readMethod = companyPD.getReadMethod();
		Object invoke = readMethod.invoke(company, null);
		System.out.println("value==>"+invoke);

		Employee employee = new Employee();
		employee.setCompany(company);
		BeanWrapperImpl employeeBeanWrapper = new BeanWrapperImpl(employee);
		PropertyDescriptor companyPD1 = employeeBeanWrapper.getPropertyDescriptor("company.companyName");

		List<Company> list = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		company.setAttrs(map);
		Company company2 = new Company();
		company2.setCompanyName("company2");
		list.add(company);
		list.add(company2);
		employee.setCompanies(list);
		PropertyDescriptor companyPD2 = employeeBeanWrapper.getPropertyDescriptor("companies[0].attrs");

		Object propertyValue1 = employeeBeanWrapper.getPropertyValue("company.companyName");
		Object propertyValue2 = employeeBeanWrapper.getPropertyValue("company.attrs[\"a\"]");
		//错误示范
		//PropertyDescriptor companyPD3 = employeeBeanWrapper.getPropertyDescriptor("company.attrs[“a”]");

		employeeBeanWrapper.setPropertyValue("company.companyName",111);
		employeeBeanWrapper.setPropertyValue("company.attrs[\"a\"]","2");
	}
	@Test
	public void testUseBeanWrapper() {
		Company company = new Company();
		BeanWrapperImpl beanWrapper = new BeanWrapperImpl(company);
		PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			System.out.println(propertyDescriptor.getName());
		}

		Class<?> attrs = beanWrapper.getPropertyType("attrs");
		System.out.println(attrs);
	}

	@Test
	public void testUseIntrospect() throws IntrospectionException {
		PropertyDescriptor[] propertyDescriptors1 = Introspector.getBeanInfo(Class.class).getPropertyDescriptors();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors1) {
			System.out.println(propertyDescriptor.getName());
		}
	}

	@Test
	public void testInstanceOf() {
		String x = "x";
		//支持多层
		print(String.class.isInstance(x), Object.class.isInstance(x), Math.class.isInstance(x));
		//支持多层
		print(x instanceof String, x instanceof Object, String.class.isAssignableFrom(x.getClass()));
		System.out.println(Object.class.isAssignableFrom(x.getClass()));
		System.out.println(Math.class.isAssignableFrom(x.getClass()));

		print(int.class.isPrimitive(), String.class.isPrimitive(), Company.class.isPrimitive());
	}

	private void print(boolean primitive, boolean primitive2, boolean primitive3) {
		System.out.println(primitive);
		System.out.println(primitive2);
		System.out.println(primitive3);
	}

	@Test
	public void testModifier() {
		Company company = new Company();
		BeanWrapperImpl beanWrapper = new BeanWrapperImpl(company);
		PropertyDescriptor id = beanWrapper.getPropertyDescriptor("id");
		Method readMethod = id.getReadMethod();
		System.out.println(readMethod.getModifiers());
		System.out.println(Modifier.isPublic(readMethod.getModifiers()));
		//只有public才可以，默认的也不行
		System.out.println(Modifier.isPublic(readMethod.getDeclaringClass().getModifiers()));
	}

	@Test
	public void testPrivateMethod() {
		Company company = new Company();
		BeanWrapperImpl beanWrapper = new BeanWrapperImpl(company);
		PropertyDescriptor id = beanWrapper.getPropertyDescriptor("id");
		//私有方法不能获取
		Method writeMethod = id.getWriteMethod();
		if (writeMethod != null) {
			System.out.println(writeMethod.getDeclaringClass());
		} else {
			System.out.println("setId is private ");
		}

		try {
			//如果用getMethod则报错。
			Method setId = Company.class.getDeclaredMethod("setId", String.class);
			System.out.println(setId);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}

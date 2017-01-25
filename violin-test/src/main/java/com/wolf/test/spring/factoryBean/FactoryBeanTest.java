package com.wolf.test.spring.factoryBean;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Description:
 * BeanFactory是个 Factory ，也就是 IOC 容器或对象工厂， FactoryBean 是个 Bean 。在 Spring 中，
 * 所有的Bean 都是由 BeanFactory( 也就是 IOC 容器 ) 来进行管理的。但对 FactoryBean 而言，
 * 这个 Bean 不是简单的Bean ，而是一个能生产或者修饰对象生成的工厂 Bean, 它的实现与设计模式中的工厂模式和修饰器模式类似。
 * <br/> Created on 2016/10/20 8:58
 *
 * @author 李超()
 * @since 1.0.0
 */
//集成junit和spring,靠DependencyInjectionTestExecutionListener加载依赖的bean
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class FactoryBeanTest extends AbstractJUnit4SpringContextTests {
//public class FactoryBeanTest {

	@Autowired
	Car car;

	@Test
	public void testJunitIntegrateSpring(){
		System.out.println(car.getBrand() + "_" + car.getMaxSpeed() + "_" + car.getPrice());
	}

	@Test
	public void testFactoryBean() {

		ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:applicationContext-test.xml");
		PersonService personService = ctx.getBean("factoryBeanTest", PersonService.class);
		personService.sayHello();

		Car car = ctx.getBean("car", Car.class);
		System.out.println(car.getBrand() + "_" + car.getMaxSpeed() + "_" + car.getPrice());

		CarFactoryBean carFactoryBean = ctx.getBean("&car", CarFactoryBean.class);
		Class<Car> objectType = carFactoryBean.getObjectType();
		System.out.println(objectType);
	}

	//通过单独创建ctx方式继承junit和spring
	@Test
	public void testInitialSpringCtx(){
		ApplicationContext ctx1 = new FileSystemXmlApplicationContext("classpath:applicationContext-test.xml");
		System.out.println(ctx1);

		ApplicationContext ctx2 = new ClassPathXmlApplicationContext(new String[] {"applicationContext-test.xml"});
		System.out.println(ctx2);

		Resource resource = new FileSystemResource("D:\\intellijWrkSpace\\violin\\violin-test\\src\\main\\resources\\applicationContext-test.xml");
		BeanFactory ctx3 = new XmlBeanFactory(resource);
		System.out.println(ctx3);

		DefaultListableBeanFactory ctx4 = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(ctx4);
		xmlBeanDefinitionReader.loadBeanDefinitions(new FileSystemResource("D:\\intellijWrkSpace\\violin\\violin-test\\src\\main\\resources\\applicationContext-test.xml"));
		System.out.println(ctx4);
	}
}

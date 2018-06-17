package com.wolf.test.spring.dependence;

import com.wolf.test.spring.aopdemo.PersonServer;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description:
 * <br/> Created on 2018/6/7 18:31
 *
 * @author 李超
 * @since 1.0.0
 */
public class OrderTest {

    @Test
    public void testDemo() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
        Results results = (Results)ctx.getBean("results");
        System.out.println(results);
    }
}

package com.wolf.test.spring.init;

import com.wolf.test.spring.aopdemo.PersonServer;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description:
 * <br/> Created on 2018/6/12 19:27
 *
 * @author 李超
 * @since 1.0.0
 */
public class SpringInitTest {

    @Test
    public void testDemo() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
    }
}

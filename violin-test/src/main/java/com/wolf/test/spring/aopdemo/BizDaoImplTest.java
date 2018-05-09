package com.wolf.test.spring.aopdemo;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description:
 * <br/> Created on 2018/5/3 10:41
 *
 * @author 李超
 * @since 1.0.0
 */
public class BizDaoImplTest {

    @Test
    public void testDemo() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
        PersonServer bean = (PersonServer) ctx.getBean("personServiceBean");
        bean.save(null);
    }

    @Test
    public void testBizDao() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
        BizDaoImpl bean = (BizDaoImpl) ctx.getBean("bizDaoImpl");
        bean.insert();
    }

}

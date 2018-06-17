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
public class AopTest {

    @Test
    public void testDemo() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
        PersonServer bean = (PersonServer) ctx.getBean("personServiceBean");
        bean.save(null);
//        bean.save2(null);//没有被拦截，内部调用save也不会被拦截，chain中只有一个ExposeInvocationInterceptor
    }

    @Test
    public void testBizDao() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
        BizDaoImpl bean = (BizDaoImpl) ctx.getBean("bizDaoImpl");
        bean.insert();
    }

    @Test
    public void testTransaction() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
        BizService bean = (BizService) ctx.getBean("bizServiceImpl");
        bean.testTransaction();
    }

    @Test
    public void testTransaction2() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
        BizService bean = (BizService) ctx.getBean("bizServiceImpl");
        bean.testTransaction2();
    }

    @Test
    public void testTransaction3() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
        BizService bean = (BizService) ctx.getBean("bizServiceImpl");
        bean.testTransaction3();
    }

    @Test
    public void testInvokeThisClassMethod() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
        BizService bean = (BizService) ctx.getBean("bizServiceImpl");
        bean.testInvokeThisClassMethod();
    }

    @Test
    public void testHashCode() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");
        PersonServer bean = (PersonServer) ctx.getBean("personServiceBean");
        bean.hashCode();
    }

}

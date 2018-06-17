package com.wolf.test.spring.aopdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * <br/> Created on 2018/6/13 14:05
 *
 * @author 李超
 * @since 1.0.0
 */
@Component
public class BizServiceImpl implements BizService {

    @Autowired
    BizDaoImpl bizDaoImpl;

    @Autowired
    BizService bizServiceImpl2;

    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public void testTransaction() {
        bizDaoImpl.insert();
        bizServiceImpl2.testTransaction();
    }

    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public void testTransaction2() {
        bizDaoImpl.insert();
        bizServiceImpl2.testTransaction2();
    }

    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public void testTransaction3() {
        bizDaoImpl.insert();
        bizServiceImpl2.testTransaction3();
    }

    //类内方法调用，不会被代理执行。代理只能执行类的方法的入口，因为被代理的是类
    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public void testInvokeThisClassMethod() {
        bizDaoImpl.insert();
        testTransaction3();
    }
}

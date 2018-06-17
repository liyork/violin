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
public class BizServiceImpl2 implements BizService {

    @Autowired
    BizDaoImpl bizDaoImpl;

    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public void testTransaction() {
        bizDaoImpl.insert();
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW,isolation= Isolation.READ_COMMITTED)
    public void testTransaction2() {
        bizDaoImpl.insert();
    }

    @Transactional(propagation= Propagation.NESTED,isolation= Isolation.READ_COMMITTED)
    public void testTransaction3() {
        bizDaoImpl.insert();
    }

    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public void testInvokeThisClassMethod() {
        bizDaoImpl.insert();
        testTransaction3();
    }
}

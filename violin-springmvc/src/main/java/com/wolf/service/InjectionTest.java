package com.wolf.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * <br/> Created on 2017/10/17 18:14
 *
 * @author 李超
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class InjectionTest extends AbstractTransactionalJUnit4SpringContextTests {

}

package com.wolf.test.log4j;

import com.wolf.utils.log.LogUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/4/25
 * Time: 13:25
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Log4jTest {

	static Logger logger = LoggerFactory.getLogger(Log4jTest.class);
	static Logger customLogger = LoggerFactory.getLogger("customLog");

	@Test
	public void testBase() {
		logger.debug("logger ... main");
		customLogger.debug("customLogger ... main");
		System.out.println("main");
		new Log4jTest().test1();
	}

	@Test
	public void testChain() {
		LogUtils.info("xxxxxxx");
	}

	public void test1(){
		System.out.println("test1");
		test2();
		logger.info("logger ... test1");
		customLogger.info("customLogger ... test1");
	}

	public void test2(){
		System.out.println("test2");
		logger.warn("logger ... test2");
		customLogger.warn("customLogger ... test2");
		new A().test3();
	}

	@Test
	public void testDynamicSwitchLogProperty() {
		logger.error("xxxxxx");
		new PropertyConfigurator().doConfigure("D:\\intellijWrkSpace\\violin\\violin-utils\\src\\main\\resources\\log4j-2.properties",
				LogManager.getLoggerRepository());
		logger.error("yyyyyy");
	}

	@Test
	public void testInvokeExceptionToString(){
		logger.error("",new RuntimeException("xxx"));
	}

}



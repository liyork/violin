package com.wolf.test.log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/21
 * Time: 16:44
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
class A{

	static Logger logger = LoggerFactory.getLogger(Log4jTest.class);
	static Logger customLogger = LoggerFactory.getLogger("customLog");

	public void test3(){
		System.out.println("test3");
		logger.error("logger ... test3");
		customLogger.error("customLogger ... test3");
	}
}

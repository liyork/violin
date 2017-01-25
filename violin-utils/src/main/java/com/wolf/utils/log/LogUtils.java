package com.wolf.utils.log;

import org.slf4j.spi.LocationAwareLogger;

/**
 * <p> Description: 统一log日志打印，内部实现传入FQCN，即打印LogUtil的上一层调用,log4j底层通过FQCN查询调用链树返回对应的输出地点
 * 例如：
 * logger.info          2016-04-26 09:55:56 com.car.base.LogUtil@(Log4jTest.java:73)    输出位置在LogUtil
 * customLogger.log     2016-04-26 09:58:17 com.car.base.Log4jTest@(Log4jTest.java:30)  输出位置在Log4jTest
 * <p/>
 * Date: 2016/4/26
 * Time: 10:03
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class LogUtils {

	static final CustomLogger customLogger = CustomLoggerFactory.getCustomLogger("customLog");

	public static void info(String content){
		customLogger.log(LogUtils.class.getName(), LocationAwareLogger.INFO_INT, content,null);
	}
	public static void info(String content,Throwable t){
		customLogger.log(LogUtils.class.getName(), LocationAwareLogger.INFO_INT, content,t);
	}

	public static void error(String content){
		customLogger.log(LogUtils.class.getName(), LocationAwareLogger.ERROR_INT, content,null);
	}
	public static void error(String content,Throwable t){
		customLogger.log(LogUtils.class.getName(), LocationAwareLogger.ERROR_INT, content,t);
	}
}

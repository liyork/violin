package com.wolf.utils.log;

/**
 * <p> Description:扩展 slf4j 接口
 * 实现 获取自定义类 行号等
 * <p/>
 * Date: 2016/4/26
 * Time: 9:11
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface CustomLogger extends org.slf4j.Logger {

	/**
	 * 自定义 callerFQCN 实现
	 * LocationAwareLogger
	 * <p/>
	 * LocationAwareLogger.TRACE_INT
	 * LocationAwareLogger.DEBUG_INT
	 * LocationAwareLogger.INFO_INT
	 * LocationAwareLogger.WARN_INT
	 * LocationAwareLogger.ERROR_INT
	 * <p/>
	 * <br/> Created on 2015-8-13 上午11:03:31
	 *
	 * @param callerFQCN，类名称，例：Log4jLoggerAdapter.class.getName();
	 * @param level
	 * @param msg
	 * @param t
	 * @since 4.0
	 */
	void log(String callerFQCN, int level, String msg,
			 Throwable t);


}

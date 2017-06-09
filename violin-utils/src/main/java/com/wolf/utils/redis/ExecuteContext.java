package com.wolf.utils.redis;

/**
 * 执行上下文
 *
 * <br/> Created on 2014-9-16 下午2:32:51
 * @since 4.1
 */
public class ExecuteContext {

    public static ThreadLocal<String> COMMAND_THREAD_LOCAL = new ThreadLocal<String>();
}

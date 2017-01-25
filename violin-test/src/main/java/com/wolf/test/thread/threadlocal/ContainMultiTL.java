package com.wolf.test.thread.threadlocal;

/**
 * Description:
 * <br/> Created on 2016/8/10 10:14
 *
 * @author 李超()
 * @since 1.0.0
 */
public class ContainMultiTL {

	ThreadLocal<String> threadLocal1 = new ThreadLocal<String>(){
		@Override
		protected String initialValue() {
			return "a";
		}
	};
	ThreadLocal<String> threadLocal2 = new ThreadLocal<String>(){
		@Override
		protected String initialValue() {
			return "b";
		}
	};
}

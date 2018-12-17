package com.wolf.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:包围handlerAdapter#handle
* <br/> Created on 2016/9/11 16:52
 *xx
 * @author 李超()
 * @since 1.0.0
 */
public class MyInterceptor3 implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0,
								HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		System.out.println("afterCompletion...");
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
						   Object arg2, ModelAndView arg3) throws Exception {
		System.out.println("postHandle...");
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
							 Object arg2) throws Exception {
		System.out.println("preHandle...");
		return true;
	}

}

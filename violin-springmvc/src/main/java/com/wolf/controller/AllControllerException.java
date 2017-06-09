package com.wolf.controller;

import org.springframework.web.servlet.ModelAndView;

/**
 * Description:所有control内请求遇到异常
 * <br/> Created on 2016/9/11 16:49
 *
 * @author 李超()
 * @since 1.0.0
 */
//@ControllerAdvice
public class AllControllerException {

	//@ExceptionHandler
	public ModelAndView exceptionHandler(Exception ex){
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("exceptionall", ex);
		System.out.println("exceptionall in testControllerAdvice");
		return mv;
	}
}

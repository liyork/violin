package com.wolf.exceptionresolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 * <br/> Created on 2016/12/2 8:36
 *
 * @author 李超()
 * @since 1.0.0
 */
@Component
public class MyExceptionResolver extends ExceptionHandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(MyExceptionResolver.class);

	public MyExceptionResolver() {
		//可能换用其他设定order方式，因为MyExceptionResolver会被parentctx加载，所以会在springmvc的三个默认值之后,然后又会被sort
		super.setOrder(0);
	}

	@Override
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
		logger.error("MyExceptionResolver............",exception);
		return super.doResolveHandlerMethodException(request, response, handlerMethod, exception);
	}
}

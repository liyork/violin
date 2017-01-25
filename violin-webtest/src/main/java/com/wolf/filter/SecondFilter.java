package com.wolf.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Description:
 * <br/> Created on 2016/12/6 8:54
 *
 * @author 李超()
 * @since 1.0.0
 */
public class SecondFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("SecondFilter...");
		//调用链使用pos定位当前chain，内部判断如果为最后一个就直接调用servlet
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}

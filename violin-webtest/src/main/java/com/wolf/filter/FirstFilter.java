package com.wolf.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import java.io.IOException;

/**
 * Description:
 * <br/> Created on 2016/12/6 8:54
 *
 * @author 李超()
 * @since 1.0.0
 */
public class FirstFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("FirstFilter...");
		String username = request.getParameter("username");
		if (StringUtils.isNoneEmpty(username) && username.equals("abc")) {
			//调用下一个filter或者servlet
			chain.doFilter(request, response);
		}else{
			//HTTP/1.1 200 OK
			//Server: Apache-Coyote/1.1
			//Content-Length: 0
			//Date: Tue, 13 Feb 2018 07:54:33 GMT
			System.err.println("filter error to errorPage...");
		}
	}

	@Override
	public void destroy() {

	}
}

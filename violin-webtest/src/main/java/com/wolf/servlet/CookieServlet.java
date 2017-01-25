package com.wolf.servlet;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 * <br/> Created on 2016/12/6 8:35
 *
 * @author 李超()
 * @since 1.0.0
 */
public class CookieServlet extends HttpServlet {

	private static final long serialVersionUID = -1915463532411657451L;

	@Override
	protected void doGet(HttpServletRequest request,
						 HttpServletResponse response) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println(JSON.toJSONString(cookie));
		}

		Cookie cookie = new Cookie("sessionId","123456789");
		cookie.setMaxAge(-30);
		response.addCookie(cookie);
	}

	@Override
	protected void doPost(HttpServletRequest request,
						  HttpServletResponse response) throws ServletException, IOException {
		//Do some other work
	}

	@Override
	public String getServletInfo() {
		return "SecondServlet";
	}
}

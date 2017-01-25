package com.wolf.servlet;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Description:
 * <p>
 * <br/> Created on 2016/12/6 8:35
 *
 * @author 李超()
 * @since 1.0.0
 */
public class MyFirstServlet extends HttpServlet {

	private static final long serialVersionUID = -1915463532411657451L;

	@Override
	protected void doGet(HttpServletRequest request,
						 HttpServletResponse response) throws ServletException, IOException {

		String type = request.getParameter("type");
		if (StringUtils.isEmpty(type)) {
			output(request, response);
		} else {
			if (type.equals("1")) {
				//不输出
				output(request, response);
				//http://localhost:8080/webtest/myFirstServlet?username=abc&type=1
				//Status Code:200 OK
				getServletContext().getRequestDispatcher("/secondServlet").forward(request, response);
			} else if (type.equals("2")) {
				//内部站点servlet：http://localhost:8080/webtest/myFirstServlet?username=abc&type=2
				//第一个请求response Status Code:302 Found  Location: http://localhost:8080/webtest/secondServlet?username=abc
				//第二个请求response Status Code:200 OK
				response.sendRedirect(request.getContextPath()+"/secondServlet?username=abc");
			} else if (type.equals("3")) {
				//内部站点jsp：http://localhost:8080/webtest/myFirstServlet?username=abc&type=3
				response.sendRedirect("index.jsp?username=abc");
			} else if (type.equals("4")) {
				//外部站点：
				//Status Code:302 Found     response header:Location: http://www.baidu.com
				response.sendRedirect("http://www.baidu.com");
			}else if (type.equals("5")) {
				//输出
				output(request, response);
				//http://localhost:8080/webtest/myFirstServlet?username=abc&type=5
				getServletContext().getRequestDispatcher("/secondServlet").include(request, response);
			}
			//整体doget方法执行完tomcat才执行跳转
			System.out.println("111111111");
		}
	}

	private void output(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			// Write some content
			out.println("<html>");
			out.println("<head>");
			out.println("<title>MyFirstServlet</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h2>Servlet MyFirstServlet at " + request.getContextPath() + "</h2>");
			out.println("</body>");
			out.println("</html>");
		} finally {
			//out.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
						  HttpServletResponse response) throws ServletException, IOException {
		//Do some other work
	}

	@Override
	public String getServletInfo() {
		return "MyFirstServlet";
	}
}

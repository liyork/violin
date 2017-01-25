package com.wolf.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Description:
 * <br/> Created on 2016/12/6 8:35
 *
 * @author 李超()
 * @since 1.0.0
 */
public class SecondServlet extends HttpServlet {

	private static final long serialVersionUID = -1915463532411657451L;

	@Override
	protected void doGet(HttpServletRequest request,
						 HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			// Write some content
			out.println("<html>");
			out.println("<head>");
			out.println("<title>SecondServlet</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h2>Servlet SecondServlet at " + request.getContextPath() + "</h2>");
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
		return "SecondServlet";
	}
}

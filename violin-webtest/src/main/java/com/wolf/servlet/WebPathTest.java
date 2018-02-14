package com.wolf.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 * Web工程的根目录也是在classes下
 * DownloadServlet.class.getResourceAsStream("/download.txt");
 * D:\intellijWrkSpace\violin\violin-webtest\target\violin-webtest-1.0-SNAPSHOT\WEB-INF\classes
 * <p>
 * <br/> Created on 2016/8/9 8:19
 *
 * @author 李超()
 * @since 1.0.0
 */
public class WebPathTest extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("get");

        String contextPath = req.getContextPath();
        System.out.println("contextPath==>" + contextPath);
        String servletPath = req.getServletPath();
        System.out.println("servletPath==>" + servletPath);
        StringBuffer requestURL = req.getRequestURL();
        System.out.println("url==>" + requestURL.toString());
        String requestURI = req.getRequestURI();
        System.out.println("uri==>" + requestURI);
        String pathInfo = req.getPathInfo();
        System.out.println("pathInfo==>" + pathInfo);
        String realPath = this.getServletContext().getRealPath("/");
        System.out.println("realPath==>" + realPath);

        InputStream in2 = DownloadServlet.class.getResourceAsStream("/download.txt");
        System.out.println(in2);
    }
}

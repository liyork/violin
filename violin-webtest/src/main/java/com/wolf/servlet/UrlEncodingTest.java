package com.wolf.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Description:
 * jsp页面中编码两次这个解决办法太愚蠢，使用过滤器可替代。主要还是不了解内部原理
 * post使用过滤器处理中文
 * get或者超链接使用urlencoder
 * <p>
 * * 网页中的表单使用POST方法提交时，数据内容的类型是 application/x-www-form-urlencoded，这种类型会：
 * 1.字符"a"-"z"，"A"-"Z"，"0"-"9"，"."，"-"，"*"，和"_" 都不会被编码;
 * 2.将空格转换为加号 (+) ;
 * 3.将非文本内容转换成"%xy"的形式,xy是两位16进制的数值;
 * 4.在每个 name=value 对之间放置 & 符号。
 * <br/> Created on 2016/8/9 8:19
 * @author 李超()
 * @since 1.0.0
 */
public class UrlEncodingTest extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("get");

		method1(req, "get");

		//?后面的参数，原生的，没有经过过滤%
		String queryString = req.getQueryString();
		System.out.println("queryString==>" + queryString);

		System.out.println("urlDecoder username==>" +
				URLDecoder.decode(queryString.substring(queryString.indexOf("=") + 1, queryString.length()), "UTF-8"));
	}

	/**
	 * 浏览器中：Content-Type:application/x-www-form-urlencoded
	 * 将中文转成utf-8，在每个字节前追加一个"%"得到编码
	 *
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		method1(req, "post");
//		method2(req,"post");
	}

	private void method1(HttpServletRequest req, String type) throws UnsupportedEncodingException {
		System.out.println(type + " method2");

		System.out.println("default encoding==>" + req.getCharacterEncoding());

		//tomcat底层收到请求后去掉%，再按照默认iso-8859-1将byte转成了string，
		String username = req.getParameter("username");
		//我们再给他转回去byte
		byte[] bytes = username.getBytes("iso-8859-1");
		//转成string使用utf-8
		String s = new String(bytes, "utf-8");
		System.out.println(s);
	}

	private void method2(HttpServletRequest req, String type) throws UnsupportedEncodingException {
		System.out.println(type + " method1");
		//设定使用utf-8转换,这就是为什么使用utf-8过滤器的作用
		req.setCharacterEncoding("utf-8");
		String username = req.getParameter("username");
		System.out.println(username);
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(URLEncoder.encode("中", "UTF-8"));//%E4%B8%AD
		String strTest = "?=abc?中%1&2<3,4>";
		strTest = URLEncoder.encode(strTest, "UTF-8");
		System.out.println(strTest);
		strTest = URLDecoder.decode(strTest, "UTF-8");
		System.out.println(strTest);
	}
}

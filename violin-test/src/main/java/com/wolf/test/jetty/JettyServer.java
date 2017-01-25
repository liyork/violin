package com.wolf.test.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;


/**
 * <p> Description:
 * /**
 * --------------------------------------------------------------------------------------------------------------
 * Jetty简介
 * Jetty是一个嵌入式的Web服务器
 * --------------------------------------------------------------------------------------------------------------
 * Jetty与Tomcat比较(取自网络)
 * Jetty的架构要比Tomcat简单一些,它是基于Handler来实现的,它可以通过Handler来进行扩展
 * Tomcat的架构是基于容器设计的,扩展Tomcat时需要了解Tomcat的整体设计结果,故不易扩展
 * --------------------------------------------------------------------------------------------------------------
 * Jetty使用
 * 0)版本选择:由于Jetty9需要JDK7的支持,所以我们这里使用jetty-distribution-8.1.10.v20130312.zip
 * 1)修改端口:修改\\JETTY_HOME\\etc\\jetty.xml第40行default="8080"即可
 * 2)非嵌入式的项目发布(有两种方式)
 * 非嵌入式的启动方式为命令行下执行该命令-->D:\Develop\jetty-distribution-8.1.10.v20130312>java -jar start.jar
 * 第一种:项目打成war包放到\\JETTY_HOME\\webapps\\下即可,访问地址为http://127.0.0.1:8080/warName(war名字大小写严格相同)
 * 第二种:类似于Tomcat的<Context path="" docBase="">的方式,即在\\JETTY_HOME\\contexts\\目录下新增一个名字随意的xml文件
 * 文件格式与该目录下的javadoc.xml相同,其中主要修改以下两处
 * <Set name="contextPath">/testBbb</Set>
 * <Set name="resourceBase">F:/Tool/Code/JavaSE/loginManager/WebRoot</Set>
 * 3)嵌入式的项目发布
 * 也就是把Jetty提供的jar加入到项目中(可以是Java项目或Web项目),然后编写通过一个main()启动Jetty,所用到的jar如下
 * JETTY_HOME中的lib目录,和lib下的jsp目录,这俩目录中的jar加入到项目中即可(若不涉及jsp页面,就不需要jsp目录下的jar了)
 * 具体写法详见这里的startForServlet()和startForWebApp()方法
 * <p/>
 * Date:2016/8/6
 * Time:10:17
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */

public class JettyServer {

	public static void main(String[] args) throws Exception {
		startServletServer(8080, "/testJettyDemo");
		startWebAppServer(8081, "/gbook");

	}


	/**
	 * 针对Servlet,应用服务器
	 * 通过servletMap参数可以使得该方法直接同时发布多个Servlet
	 *
	 * @param port        访问服务器的端口
	 * @param contextPath 访问服务器的地址
	 */
	private static void startServletServer(int port, String contextPath) {
		//绑定端口
		Server server = new Server(port);
		//可以使用ServletContextHandler处理Servlet
		ServletContextHandler contextHandler = new ServletContextHandler();
		//添加Servlet并指定映射url-pattern
		contextHandler.addServlet(UserServlet.class, "/user");
		contextHandler.addServlet(LoginServlet.class, "/login");
		//此时访问路径就是http://127.0.0.1:port/contextPath/urlPattern
		contextHandler.setContextPath(contextPath);
		//绑定Handler
		server.setHandler(contextHandler);
		//启动服务
		try {
			server.start();
		} catch (Exception e) {
			System.out.println("启动Jetty时发生异常,堆栈轨迹如下");
			e.printStackTrace();
		}
		if (server.isStarted()) {
			System.out.println("Servlet服务启动成功");
		}
	}


	/**
	 * 针对一个Web应用，web服务器
	 * 注意resourceBase参数指向的应用所依赖的jar必须全部存放在其WebRoot\WEB-INF\lib目录中
	 * 否则应用启动后,访问时会由于在lib中找不到jar而报告java.lang.ClassNotFoundException
	 *
	 * @param port        访问服务器的端口
	 * @param contextPath 访问服务器的地址
	 */
	private static void startWebAppServer(int port, String contextPath) {
		Server server = new Server(port);
		WebAppContext context = new WebAppContext();
		context.setContextPath(contextPath);
		//使用WebAppContext时就必须设置resourceBase
		//Web应用的目录(需指向到WebRoot目录下)
		//此时访问路径就是http://127.0.0.1:port/contextPath/+要访问的文件路径
		context.setResourceBase("E:\\WebRoot");
		server.setHandler(context);
		try {
			server.start();
		} catch (Exception e) {
			System.out.println("启动Jetty时发生异常,堆栈轨迹如下");
			e.printStackTrace();
		}
		if (server.isStarted()) {
			System.out.println("Web服务启动成功");
		}
	}

}

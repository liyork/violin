package com.wolf.servlet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

/**
 * <p> Description:
 * <p/>
 * Date: 2015/7/8
 * Time: 17:03
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class JndiWebTest extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void getConnection() throws Exception {

		Context context = new InitialContext();
		DataSource dataSource = (DataSource)context.lookup("java:comp/env/jdbc/mySql");
		System.out.println(dataSource);
		System.out.println("111111");

		Connection connection = dataSource.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("insert into users(name,age) values ('xx3',2)");
		preparedStatement.execute();
		connection.close();
	}

	public static void main(String[] args) throws NamingException {
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "org.apache.naming.java.javaURLContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "org.apache.naming");

//		Context initialContext = new InitialContext(props);
		Context initialContext = NamingManager.getInitialContext(props);
		initialContext.bind("com.xx.aa","aaa1111");

		Object lookup = initialContext.lookup("com.xx.aa");
		System.out.println(lookup);
	}
}

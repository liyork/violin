package com.wolf.test.base;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

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
public class JndiTest {

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
}

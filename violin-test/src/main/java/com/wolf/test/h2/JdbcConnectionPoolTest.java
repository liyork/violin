package com.wolf.test.h2;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;

/**
 * Description:
 * Created on 2021/9/23 2:44 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class JdbcConnectionPoolTest {
    public static void main(String[] args) throws Exception {
        Class.forName("org.h2.Driver");

        JdbcConnectionPool cp = JdbcConnectionPool.create("jdbc:h2:~/h2test/test", "sa", "sa");
        Connection conn = cp.getConnection();
        conn.createStatement().execute("CREATE TABLE IF not EXISTS TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))");
        conn.close();
        cp.dispose();
    }
}

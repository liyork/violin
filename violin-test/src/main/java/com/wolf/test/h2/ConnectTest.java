package com.wolf.test.h2;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Description:
 * Created on 2021/9/23 2:37 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class ConnectTest {
    public static void main(String[] args) throws Exception {
        Class.forName("org.h2.Driver");

        Connection conn = DriverManager.getConnection("jdbc:h2:~/h2test/test", "sa", "sa");
        // add application code here
        conn.close();
    }
}

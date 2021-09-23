package com.wolf.test.h2;

import java.sql.*;

/**
 * Description: 测试扩展函数
 * Created on 2021/9/23 3:41 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class FuncTest {
    public static void main(String[] args) throws Exception {
        // 1、注册myid函数的SQL语句，语句格式：CREATE ALIAS [IF NOT EXISTS] newFunctionAliasName [DETERMINISTIC] FOR classAndMethodName
        String sql = "CREATE ALIAS IF NOT EXISTS myid FOR \"com.wolf.test.h2.H2DBFunctionExt.myid\"";

        // 加载H2 DB的JDBC驱动
        Class.forName("org.h2.Driver");

        // 链接数据库，自动在~创建数据库test，得到联接对象 connection
        String url = "jdbc:h2:~/h2test/test";
        Connection con = DriverManager.getConnection(url, "sa", "sa");

        // 3、获取Statement对象
        Statement stmt = con.createStatement();

        // 4、执行sql
        stmt.execute(sql);

        System.out.println("H2数据库扩展函数注册成功！");

        String selectSql = "select myid()";
        PreparedStatement prepareStatement = con.prepareStatement(selectSql);
        ResultSet rs = prepareStatement.executeQuery();
        while (rs.next()) {
            String id = rs.getString(1);
            System.out.println("结果：uuid:" + id);
        }

    }
}

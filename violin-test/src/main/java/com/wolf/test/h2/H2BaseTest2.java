package com.wolf.test.h2;

import java.sql.*;

/**
 * Description:
 * Created on 2021/9/23 3:16 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class H2BaseTest2 {
    public static void main(String[] args) throws Exception {
        // 加载H2 DB的JDBC驱动
        Class.forName("org.h2.Driver");

        // 链接数据库，自动在~创建数据库test，得到联接对象 connection
        String url = "jdbc:h2:~/h2test/test";
        Connection con = DriverManager.getConnection(url, "sa", "sa");

        // 新建数据表
        String ctreateTable = "DROP TABLE test IF EXISTS; create table test(id integer,name VARCHAR(100) )";
        Statement createStatement = con.createStatement();
        long f1 = createStatement.executeUpdate(ctreateTable);
        System.out.println("创建表：" + f1);

        // 插入数据
        String insertSql = "INSERT INTO test VALUES(1,'xxx')";
        Statement insertStatement = con.createStatement();
        long f2 = insertStatement.executeUpdate(insertSql);
        System.out.println("插入数据：" + f2);

        // 查询数据
        String selectSql = "select id,name from test";
        PreparedStatement prepareStatement = con.prepareStatement(selectSql);
        // 发送SQL 返回一个ResultSet
        ResultSet rs = prepareStatement.executeQuery();

        // 编历结果集
        while (rs.next()) {// 从数据库的取一行数据，是否还有下一行
            int id = rs.getInt(1); // 列从1开始
            String name = rs.getString(2);
            System.out.println("结果：id:" + id + "\t名称:" + name);
        }

        // 关闭连接
        con.close();
    }
}

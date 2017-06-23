package com.wolf.test.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Description:
 * <br/> Created on 2017/6/21 8:36
 *
 * @author 李超
 * @since 1.0.0
 */
public class MySqlBatchInsert {

    public static void main(String[] args) throws Exception {
        batchInsert();
    }


    public static void batchInsert() throws Exception {
        long start = System.currentTimeMillis();
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?useServerPrepStmts=false&rewriteBatchedStatements=true", "root", "");

        connection.setAutoCommit(false);
        PreparedStatement cmd = connection.prepareStatement("insert into bigtable(name,age,mark) values(?,?,?)");

        for(int i = 0; i < 5000000; i++) {//100万条数据
            cmd.setString(1, "name" + i);
            cmd.setInt(2, i);
            cmd.setString(3, "mark" + i);
            cmd.addBatch();
            if(i % 1000 == 0) {
                cmd.executeBatch();
            }
        }
        cmd.executeBatch();
        connection.commit();

        cmd.close();
        connection.close();

        long end = System.currentTimeMillis();
        System.out.println("批量插入需要时间:" + (end - start)); //批量插入需要时间:4675
    }
}

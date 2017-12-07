package com.wolf.test.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;

/**
 * Description:
 * <br/> Created on 2017/6/21 8:36
 *
 * @author 李超
 * @since 1.0.0
 */
public class MySqlBatchInsert {

    public static void main(String[] args) throws Exception {
        batchInsert1();
//        batchInsert2("class1");
//        batchInsert2("book");
//        batchInsert2("phone");
    }


    /**
     * 结论：如果使用setAutoCommit需要配合commit一起提交
     * 否则一起都不用executeBatch每次执行一批修改后sql的数据
     * @throws Exception
     */
    public static void batchInsert1() throws Exception {
        long start = System.currentTimeMillis();
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/testdb?useServerPrepStmts=false&rewriteBatchedStatements=true", "root", "123456");

        //这步很关键，下面即使执行了executeBatch也不会发送sql，只有在commit时才会一起提交
        //后来测试发现setAutoCommit只是保证原子性让所有sql都一起成功或失败。executeBatch是把sql改写成insert xx values(),()，而auto=true最后结果是一批一执行
        //connection.setAutoCommit(false);
        PreparedStatement cmd = connection.prepareStatement("insert into bigtable(name,age,mark) values(?,?,?)");

        for(int i = 0; i < 5000000; i++) {//100万条数据
            cmd.setString(1, "name" + i);
            cmd.setInt(2, i);
            cmd.setString(3, "mark" + i);
            cmd.addBatch();
            if(i != 0 && i % 1000 == 0) {//i!=0防止第一次0就插入一遍
                cmd.executeBatch();
                //connection.commit();
            }
        }
        cmd.executeBatch();
        //connection.commit();

        cmd.close();
        connection.close();

        long end = System.currentTimeMillis();
        System.out.println("批量插入需要时间:" + (end - start)); //批量插入需要时间:4675
    }

    public static void batchInsert2(String tableName) throws Exception {
        long start = System.currentTimeMillis();
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?useServerPrepStmts=false&rewriteBatchedStatements=true", "root", "");

        connection.setAutoCommit(false);
        String sql = "insert into " + tableName + "(card) values(?)";
        PreparedStatement cmd = connection.prepareStatement(sql);

        Random random = new Random();
        for(int i = 0; i < 10000; i++) {
            cmd.setInt(1, random.nextInt(20));
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

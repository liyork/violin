package com.wolf.test.h2;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Description:
 * Created on 2021/9/23 3:16 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class H2BaseTest {
    public static void main(String[] args) throws Exception {
        // 内存模式不会落地持久化，所以比较适合测试，关闭连接后数据库就清空
        String JDBC_URL = "jdbc:h2:mem:testmem;DB_CLOSE_DELAY=-1";

        // 持久化模式，会将数据落地持久化到指定的目录，生成与数据库同名的.mv.db文件
        String JDBC_URL1 = "jdbc:h2:./test";

        // 服务式就是指定一个tcp的远程目录
        String JDBC_URL2 = "jdbc:h2:tcp://1.1.1.1/~/test”；";

        // 连接参数
        //用户名
        String USER = "sa";
        //连接数据库时使用的密码，默认为空
        String PASSWORD = "sa";
        //连接H2数据库时使用的驱动类，org.h2.Driver这个类是由H2数据库自己提供的，在H2数据库的jar包中可以找到
        String DRIVER_CLASS = "org.h2.Driver";

        // 创建连接
        // 加载H2数据库驱动
        Class.forName(DRIVER_CLASS);
        // 根据连接URL，用户名，密码获取数据库连接
        Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);

        // 自定义函数，创建一个类，类里定义一个静态的公共方法，然后执行下面的语句就可以添加一个函数了
        // functionNameInH2为H2中使用的名字，不能重名，package.className.functionNameInJava则是方法的路径，从包名到方法名都需要加上
        // CREATE ALIAS IF NOT EXISTS functionNameInH2 FOR "package.className.functionNameInJava"

    }
}

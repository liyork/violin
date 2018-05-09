package com.wolf.test.spring.aopdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Description:bean若有接口则用动态代理，若无则用cglib
 * <br/> Created on 2018/5/3 10:41
 *
 * @author 李超
 * @since 1.0.0
 */
@Repository
public class BizDaoImpl {

    public void insert() {
        System.out.println("insert xxxx");
    }
}

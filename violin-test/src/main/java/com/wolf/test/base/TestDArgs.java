package com.wolf.test.base;

import java.util.Properties;

/**
 * Description:
 * -Dxxx123="abc"
 * <br/> Created on 2018/1/31 9:54
 *
 * @author 李超
 * @since 1.0.0
 */
public class TestDArgs {

    public static void main(String[] args) {
        Properties properties = System.getProperties();
        String monitorConf = properties.getProperty("xxx123");
        System.out.println(monitorConf);
    }
}

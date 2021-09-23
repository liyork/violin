package com.wolf.test.h2;

import java.util.UUID;

/**
 * Description: 对H2数据库函数的扩展
 * Created on 2021/9/23 3:40 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class H2DBFunctionExt {
    public static String myid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}

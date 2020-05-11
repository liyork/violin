package com.wolf.test.base;

import org.junit.Test;

/**
 * Description:
 *
 * @author 李超
 * @date 2020/05/11
 */
public class SystemTest {

    // 属于VM options，-Dxxx=2222
    // java [ options ] class [ argument...  ]
    // -Dproperty=value  Sets a system property value.
    @Test
    public void testUseSystemProperty() {
        System.out.println("print xxx:" + System.getProperty("xxx"));
    }

    // 在idea的Environment varables中设定qq=123
    @Test
    public void testUseSystemEnv() {
        System.out.println("print qq:" + System.getenv().get("qq"));
    }

}

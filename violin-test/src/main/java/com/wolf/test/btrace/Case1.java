package com.wolf.test.btrace;

import java.util.Random;

/**
 * Description:
 * <br/> Created on 2018/1/26 16:37
 *
 * @author 李超
 * @since 1.0.0
 */
public class Case1 {

    public static void main(String[] args) throws Exception {
        Random random = new Random();
        CaseObject object = new CaseObject();
        boolean result = true;
        while (result) {
            result = object.execute(random.nextInt(1000));
            Thread.sleep(1000);
        }
    }
}

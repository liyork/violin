package com.wolf.test.btrace;

import java.util.Random;
import java.util.UUID;

/**
 * Description:
 * <br/> Created on 2018/1/26 10:52
 *
 * @author 李超
 * @since 1.0.0
 */
public class RemoteClass {

    public String f1(String a, int b) {
        System.out.println(a + " " + b);
        return a;
    }

    public static void main(String[] args) {
        RemoteClass rc = new RemoteClass();
        while (true) {
            rc.f1(UUID.randomUUID().toString(), new Random().nextInt());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
    }
}

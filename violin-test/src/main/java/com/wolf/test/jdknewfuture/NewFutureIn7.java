package com.wolf.test.jdknewfuture;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <br/> Created on 2017/12/19 9:35
 *
 * @author 李超
 * @since 1.0.0
 */
public class NewFutureIn7 {

    public static void main(String[] args) {
        supportStringInSwitch();

        autoInfer();

    }

    private static void autoInfer() {
        List<String> list = new ArrayList<>();
    }

    private static void supportStringInSwitch() {
        String s = "test";

        switch (s) {
            case "test":
                System.out.println("test");
            case "test1":
                System.out.println("test1");
                break;
            default:
                System.out.println("break");
                break;
        }
    }
}

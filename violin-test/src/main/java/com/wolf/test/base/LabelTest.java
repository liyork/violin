package com.wolf.test.base;

/**
 * Description:
 * <br/> Created on 2017/6/15 22:15
 *
 * @author 李超
 * @since 1.0.0
 */
public class LabelTest {

    public static void main(String[] args) {

        xx:
        for(int x = 0; x < 10; x++) {
            System.out.println("x: " + x);
            for(int i = 0; i < 50; i++) {
                if(i == 20) {
                    System.out.println(i);
                    break xx;//跳到最外层循环并执行循环后的语句
                }
            }
        }

        System.out.println("1111");

        qq:
        for(int y = 0; y < 3; y++) {
            System.out.println("y: " + y);
            for(int i = 0; i < 30; i++) {
                System.out.println("i:"+i);
                if(i == 2) {
                    System.out.println(i);
                    continue qq;//跳到最外层循环并执行外层循环的下次
                }
            }
        }

    }
}

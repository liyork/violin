package com.wolf.test.writeexam;

/**
 * Description:
 * <br/> Created on 2017/5/2 17:47
 *
 * @author 李超
 * @since 1.0.0
 */
public class RecursiveTest {

    public static void main(String[] args) {
//        printIncrease(1237);

//        int age = getAge(4);
//        System.out.println(age);

        toBinary(9);
    }

    private static void printIncrease(int i) {
        System.out.println(i);
        if(i <= 5000) {
            printIncrease(2 * i);
        }
        System.out.println(i);
    }

    private static int getAge(int i) {
        if(i == 1) {
            return 10;
        }
        return getAge(i-1)+2;
    }

    private static void toBinary(int i) {
        int n = i / 2;
        if(n != 0) {
            toBinary(n);
        }
        System.out.print(i%2);
    }

}

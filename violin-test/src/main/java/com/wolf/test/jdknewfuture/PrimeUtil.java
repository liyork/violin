package com.wolf.test.jdknewfuture;

/**
 * Description:
 * <br/> Created on 24/03/2018 9:38 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class PrimeUtil {

    public static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(i+" "+isPrime(i));
        }
    }
}

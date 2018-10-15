package com.wolf.test.base.proxy;

/**
 * Description:
 * <br/> Created on 18/09/2018 12:25 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class UserDaoImpl implements UserDao {
    public void test() {
        System.out.println("test....");
    }

    @Override
    public int hashCode() {
        System.out.println("sub hashcode begin.............");
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            System.out.println(stackTraceElement);
        }

        return 1111;
    }
}
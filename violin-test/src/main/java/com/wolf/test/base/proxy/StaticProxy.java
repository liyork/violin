package com.wolf.test.base.proxy;

/**
 * Description:静态代理。一个代理对应一个接口和实现类。
 * 类可能很多。一旦接口增加方法,目标对象与代理对象都要维护.
 * <br/> Created on 18/09/2018 12:28 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class StaticProxy implements UserDao {

    private UserDao userDao;

    public StaticProxy(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void test() {
        System.out.println("before");
        userDao.test();
        System.out.println("after");
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        UserDao proxy = new StaticProxy(userDao);
        proxy.test();
    }
}

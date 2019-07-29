package com.wolf.test.jdknewfuture.java8inaction;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/07/21
 */
public class Trader {

    private final String name;
    private final String city;

    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }
}

package com.wolf.test.jdknewfuture.java8inaction;

/**
 * Description: lambda bean
 *
 * @author 李超
 * @date 2019/07/19
 */
public class LbBean {

    private String name;

    private int money;

    private String color;

    public LbBean() {
    }

    public LbBean(int money) {
        this.money = money;
    }

    public LbBean(int money, String color) {
        this.money = money;
        this.color = color;
    }

    public int getMoney() {
        return money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}

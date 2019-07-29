package com.wolf.test.jdknewfuture.java8inaction;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/07/21
 */
public class Transaction {

    private final Trader trader;
    private final int year;
    private final int value;
    private int currency;

    public Transaction(Trader trader, int year, int value) {
        this.trader = trader;
        this.year = year;
        this.value = value;
    }

    public Trader getTrader() {
        return trader;
    }

    public int getYear() {
        return year;
    }

    public int getValue() {
        return value;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "trader=" + trader +
                ", year=" + year +
                ", value=" + value +
                '}';
    }
}

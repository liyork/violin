package com.wolf.test.jdknewfuture.java8inaction;

import com.wolf.utils.TimeUtils;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/08/01
 */
public class Shop {

    private String name;

    private static Random random = new Random();

    public Shop(String name) {
        this.name = name;
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            double price = getPrice(product);
            completableFuture.complete(price);
        }).start();

        return completableFuture;
    }

    //开启的线程遇到异常，执行失败，但是外界不知道
    public Future<Double> getPriceAsyncErrorException(String product) {
        CompletableFuture<Double> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            double price = 7 / 0;
            completableFuture.complete(price);
        }).start();

        return completableFuture;
    }

    //开启的线程遇到异常，执行失败，包装给外界知道
    public Future<Double> getPriceAsyncRightException(String product) {
        CompletableFuture<Double> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            double price = 0;
            try {
                price = 7 / 0;
                completableFuture.complete(price);
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }

        }).start();

        return completableFuture;
    }

    //使用工厂方法
    public Future<Double> getPriceAsyncSupplier(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice2(product));
    }

    //ShopName:price:DiscountCode
    public String getPriceDiscount(String product) {
        double price = calculatePrice2(product);
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", name, price, code);
    }

    private double calculatePrice(String product) {
        TimeUtils.sleepSecond(1);
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    private double calculatePrice2(String product) {
        int i = 500 + random.nextInt(2000);
        TimeUtils.sleepMillisecond(i);
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

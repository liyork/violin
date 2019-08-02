package com.wolf.test.jdknewfuture.java8inaction;

import com.wolf.utils.TimeUtils;

/**
 * Description: 远程服务，按照参数，计算生成最后价格
 *
 * @author 李超
 * @date 2019/08/02
 */
public class Discount {
    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);
        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShopName() + " price is " +
                Discount.apply(quote.getPrice(),
                        quote.getDiscountCode());
    }

    private static double apply(double price, Code code) {
        TimeUtils.sleep(1);
        return price * (100 - code.percentage) / 100;
    }
}

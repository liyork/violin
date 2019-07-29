package com.wolf.test.jdknewfuture.java8inaction;

import java.util.function.Function;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/07/20
 */
public class Dish {

    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public int getCalories() {
        return calories;
    }

    public Type getType() {
        return type;
    }

    public enum Type {MEAT, FISH, OTHER}

    public Function<Dish, String> getCaloriesLevel() {
        return (d) -> {//lambda不太易读
            if (calories < 300) {
                return "a";
            } else if (calories < 700) {
                return "b";
            } else {
                return "c";
            }
        };
    }

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", vegetarian=" + vegetarian +
                ", calories=" + calories +
                ", type=" + type +
                '}';
    }
}

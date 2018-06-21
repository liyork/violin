package com.wolf.test.generics;

/**
 * Description:泛型不是协变的，不像数组
 * <br/> Created on 2017/1/16 16:59
 *
 * @author 李超()
 * @since 1.0.0
 */
class BasicGenericClass<T> { //泛型定义，【类型形参】

    private T t;
    private static Object object;

    //使用泛型,调用时是【类型实参】
    public void setT(T t) {
        this.t = t;
    }

    public T getX() {
        return t;
    }
}

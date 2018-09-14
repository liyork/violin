package com.wolf.test.generics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wolf.test.entity.Person;

import java.lang.reflect.Field;

/**
 * Description:泛型不是协变的，List<Integer> 不是 List<Number>
 * 不像数组Integer[] 也是 Number[]
 * <br/> Created on 2017/1/16 16:59
 *
 * @author 李超()
 * @since 1.0.0
 */
class BasicGenericClass<T,Q> { //泛型定义，【类型形参】

    private T t;
    private static Object object;

    //使用泛型,调用时是【类型实参】
    public void setT(T t) {
        this.t = t;
    }

    public T getX() {
        return t;
    }

    //这里作为父类，由于泛型在编译期过后就被擦除，所以这里仅仅是T，不知道真实的子类Person
    public void testTypeRefError(String s) {
        System.out.println("xxx==>"+this);//CustomizeClass
        T t = JSON.parseObject(s, new MyTypeReference<T>() {
        });
        System.out.println(t);
    }

    public void testTypeRefCorrect(String s) throws Exception {
        System.out.println("xxx==>"+this);
        MyTypeReference<T> type = new MyTypeReference<T>() {
        };
        Field type1 = TypeReference.class.getDeclaredField("type");
        type1.setAccessible(true);
        type1.set(type, Person.class);
        T t = JSON.parseObject(s, type);
        System.out.println(t);
    }

    //TypeReference构造函数中获取this.getClass就是MyTypeReference，他的父类的泛型就是TypeReference<T>中的T。父类是无法获取T的实际类型的
    //额外保存参数类型
    class MyTypeReference<T> extends TypeReference<T>{
        public MyTypeReference() {
            System.out.println("yyyy==>"+this);//MyTypeReference
        }

    }
}


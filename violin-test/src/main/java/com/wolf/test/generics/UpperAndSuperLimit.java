package com.wolf.test.generics;

import com.wolf.test.generics.entity.Children;
import com.wolf.test.entity.Person;
import com.wolf.test.generics.entity.SubChildren;

import java.io.Serializable;
import java.util.List;

/**
 * Description:  //上限：<? extends 父类> 下限:<? super 子类>
 * <br/> Created on 2017/1/16 16:59
 *
 * @author 李超()
 * @since 1.0.0
 */
//只能是person或其子类
class UpperAndSuperLimit<T extends Children & Serializable> {

    // 何时使用 extends 和 super，Java 有一个 PECS 原则
    // PECS 的意思是 Producer Extends, Consumer Super???

    // list中包含的是：Children及其父类
    public static void down(List<? super Children> list) {

        // 由于包含的是Children及其父类，所以不知道里面是什么，只能用Object
        for (Object o : list) {
            System.out.println(o);
        }
        // 可以放入Children及其子类???
        list.add(new Children(1, "a"));
        //list.add(new Person(1, "a"));
        list.add(new SubChildren(1, "a"));
    }

    // list中包含的是：Children及其子类
    public static void up(List<? extends Children> list) {

        // 只能知道是Children及其子类，所以只能用Children的属性
        for (Children children : list) {
            System.out.println(children.getId());
        }
        // 不能放入任何，编译器只知道是Children及其子类但是并不知道是哪个子类
        //list.add(new Children(1, "a"));
        //list.add(new Person(1, "a"));
        //list.add(new SubChildren(1, "a"));
    }

    //泛型静态方法，编译时期能猜出返回值类型
    public static <E> E testGenericMethod1(List<E> list) {
        return list.get(0);
        //既然编译时期能确定类型，调用的地方获取数据自然使用真实类型，运行期就报错了
        //return getObject();
//		return (E) "string";
    }

    static <V> V getObject() {
        return (V) new Object();
    }

    //泛型实例方法，编译时期不能猜出返回值类型
    public <E> E testGenericMethod2(List<E> list) {
        return list.get(0);
    }

    //泛型方法(尖括号应出现在方法的其它所有修饰符之后和方法的返回类型之前,也就是紧邻返回值之前)
    public static <A, B extends Person> boolean compare(CustomizeClass<A> customizeClass1, CustomizeClass<B> customizeClass2) {
        return customizeClass1.getEntity().getClass().getName().equals(customizeClass2.getEntity().getClass().getName());
    }

    //之所以声明泛型方法，一般是因为您想要在该方法的多个参数之间宣称一个类型约束
    //为什么您选择使用泛型方法，而不是将类型 T 添加到类定义呢？（至少）有两种情况应该这样做：??
    //当泛型方法是静态的时，这种情况下不能使用类类型参数。
    //当 T 上的类型约束对于方法真正是局部的时，这意味着没有在相同类的另一个 方法签名中使用相同 类型 T 的约束。通过使得泛型方法的类型参数对于方法是局部的，可以简化封闭类型的签名。
    //不知道为什么不能检查出来???通过在掉用处ctrl+p，发现原来编译器发寻找他们共同的父类，例如Serializable、Object
    public static <Q> Q ifThenElse(boolean b, Q a, Q c) {
        return b ? a : c;
    }
}

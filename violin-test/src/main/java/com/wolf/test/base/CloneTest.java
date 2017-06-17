package com.wolf.test.base;

import com.alibaba.fastjson.JSON;

/**
 * Description:
 * 创建实例步骤：
 * 1.先加载类的字节码到持久代
 * 2.根据持久代中类的信息创建对应的class对象(如果没有)
 * 3.根据class对象信息分配内存
 * 4.调用构造方法初始化变量(类中实例变量经反编译后查看都在类的构造方法中初始化字段，应该编译后都在init方法中)
 *
 * clone时不会调用构造方法，仅仅先分配内存，然后根据原来值进行属性拷贝
 * <br/> Created on 2017/6/16 23:58
 *
 * @author 李超
 * @since 1.0.0
 */
public class CloneTest implements Cloneable {

    private String name;

    private int age;

    public CloneTest() {
        System.out.println("default constructor..");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        CloneTest clone = (CloneTest) super.clone();
        clone.setAge(123456);
        return clone;
    }

    public String getName() {
        System.out.println("getName");
        return name;
    }

    public void setName(String name) {
        System.out.println("setName");
        this.name = name;
    }

    public int getAge() {
        System.out.println("getAge");
        return age;
    }

    public void setAge(int age) {
        System.out.println("setAge");
        this.age = age;
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        CloneTest cloneTest = new CloneTest();
        cloneTest.setAge(111);
        cloneTest.setName("name1");

        Object clone = cloneTest.clone();
        System.out.println(JSON.toJSONString(clone));
    }
}

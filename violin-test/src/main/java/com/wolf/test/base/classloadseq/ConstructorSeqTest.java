package com.wolf.test.base.classloadseq;

import com.alibaba.fastjson.JSON;

/**
 * Description:
 * <br/> Created on 2017/5/1 6:39
 *
 * @author 李超
 * @since 1.0.0
 */
public class ConstructorSeqTest {

    int a = 1;

    //一般不建议这么用，感觉很危险，本身实例还没有初始化完毕就给别人用了。
    //这里由于在构造器之前被执行，所以传入时属性b不会被设定值，
    // 由于和constructorSeqRef都引用了一个,所以下面设定b时两个引用都会有
    ConstructorSeqRef constructorSeqRef = new ConstructorSeqRef(this);

    //无限递归
    //ConstructorTest constructorTest = new ConstructorTest();

    int b = 2;

    public ConstructorSeqTest() {
        System.out.println("constructor ...");
    }


    public static void main(String[] args) {
        ConstructorSeqTest constructorSeqTest = new ConstructorSeqTest();
        System.out.println(JSON.toJSONString(constructorSeqTest));
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public ConstructorSeqRef getConstructorSeqRef() {
        return constructorSeqRef;
    }

    public void setConstructorSeqRef(ConstructorSeqRef constructorSeqRef) {
        this.constructorSeqRef = constructorSeqRef;
    }
}

package com.wolf.test.jvm.initialseq;

/**
 * Description: 实例化过程测试
 *
 * @author 李超
 * @date 2020/02/09
 */
public class InitialSeqDemo {

    public static void main(String... args) {
        Bar bar = new Bar();
        System.out.println(bar.getValue());
    }
}

class Foo {
    int i = 1;

    Foo() {
        System.out.println(i);
        int x = getValue();
        System.out.println(x);
    }

    {
        i = 2;
    }

    protected int getValue() {
        return i;
    }
}

//子类
class Bar extends Foo {
    int j = 1;

    Bar() {
        j = 2;
    }

    {
        j = 3;
    }

    @Override
    protected int getValue() {
        return j;
    }
}
package com.wolf.test.base;

/**
 * Description:内部类和静态内部类
 * 共同:都是和外部类高内聚，不需要另创建一个类文件，还可实现多继承
 * 区别:关键是是否需要引用其外部类中内容
 * <br/> Created on 2017/3/27 13:54
 *
 * @author 李超
 * @since 1.0.0
 */
public class InnerClassAndStaticTest {

    private int age;

    private static String name;

    public void testA() {

    }

    public static void testB() {

    }

    // 成员内部类
    public class InnerClass {

        private int aa;
        //内部类不能有static变量
//        private static int bb;

        public void test() {

        }
        //内部类不能有static方法
//        public static void test2(){
//        }

        //一般内部类对于外部类中的所有都能使用
        public void test3() {
            age = 1;
            name = "xx";
            testA();
            testB();
        }
    }

    //内部类static和非static都能有
    public static class StaticInnerClass {
        private int aa;
        private static int bb;

        public void test() {
        }

        public static void test2() {
            //静态内部类不能用外部类的非静态属性
//            age = 1;
        }

        public void test3() {
            //静态内部类不能用外部类的非静态属性
//            age = 1;
            name = "xx";
            //静态内部类不能用外部类的非静态方法
//            testA();
            testB();
        }
    }
}

class OuterClass {

    public static void main(String[] args) {
        InnerClassAndStaticTest innerClassAndStaticTest = new InnerClassAndStaticTest();
        // 成员内部类构造时需要外部类对象先被实例化
        InnerClassAndStaticTest.InnerClass innerClass = innerClassAndStaticTest.new InnerClass();
        innerClass.test();

        //静态内部类构造时不需要外部类对象
        InnerClassAndStaticTest.StaticInnerClass staticInnerClass = new InnerClassAndStaticTest.StaticInnerClass();
        staticInnerClass.test();

    }

}

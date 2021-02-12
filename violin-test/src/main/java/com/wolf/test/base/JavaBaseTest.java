package com.wolf.test.base;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * Description:
 * 接口的方法都默认是public abstract，变量都是public static final的。
 * abstract不能和final一起修饰方法，因为abstract就是为了子类重写，final就是禁止子类重写。。
 * <br/> Created on 2017/3/23 10:26
 *
 * @author 李超
 * @since 1.0.0
 */
public class JavaBaseTest {

    public static void main(String[] args) {
        //启动java的目录
        System.out.println(System.getProperty("user.dir"));
    }

    @Test
    public void testMod() {
        String s = "abc";
        int tableNum = 2;
        int dbNum = 6;
        int i1 = s.hashCode() % (tableNum * dbNum);//0 -- 11
        int i = i1 / tableNum;//0 -- 5
        System.out.println(i);
    }

    @Test
    public void testSplit() {
        String str = "a,b,c,,";
        String[] ary = str.split(",");
        // 预期大于 3，结果是 3
        System.out.println(ary.length);
    }


    @Test
    public void testEmptyBlank() {
        String s = "";
        boolean empty = StringUtils.isEmpty(s);//仅仅null或""
        System.out.println(empty);
        boolean blank = StringUtils.isBlank(s);//包含isempty和"  "
        System.out.println(blank);
    }

    @Test
    public void testFloat() {
//        float a=1.3;//若不指定f，那么就认为double

        float a = 3 * 0.1f;
        float b = 0.3f;
        System.out.println(a == b);

        System.out.println();
        // 有些浮点数不能完全精确的表示出来
        double a1 = 3 * 0.1;
        double b1 = 0.3;
        System.out.println(a1);
        System.out.println(b1);
        System.out.println(a1 == b1);
    }

    @Test
    public void testContinue() {
        int a = 0;
        for (int i = 0; i < 1; i++) {
            if (i == 0) {
                continue;// 直接继续下次循环，从头开始，不执行后面任何逻辑。
            }
            a++;
        }

        System.out.println(a);
    }

    // 支持跨层属于
    @Test
    public void testInstanceOf() {
        String x = "x";
        print(String.class.isInstance(x), Object.class.isInstance(x), Math.class.isInstance(x));
        print(x instanceof String, x instanceof Object, String.class.isAssignableFrom(x.getClass()));
        System.out.println(Object.class.isAssignableFrom(x.getClass()));
        System.out.println(Math.class.isAssignableFrom(x.getClass()));

        // 变量必须是引用类型，不能是基本类型
//        int a = 1;
//        System.out.println(a instanceof Integer);
//        System.out.println(a instanceof Object);

        Integer a = 1;
        System.out.println(a instanceof Integer);
        System.out.println(a instanceof Object);

        System.out.println();
        print(int.class.isPrimitive(), String.class.isPrimitive(), JavaBaseTest.class.isPrimitive());
    }

    private void print(boolean primitive, boolean primitive2, boolean primitive3) {
        System.out.println(primitive);
        System.out.println(primitive2);
        System.out.println(primitive3);
        System.out.println();
    }

}

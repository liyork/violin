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
    }

    //靠近整数，1.5向正进=2，-1.5也向正进=-1
    //-1.6  -1.5  -1.4  0  1.4  1.5  1.6
    @Test
    public void testRountd() {
        System.out.println(Math.round(-1.6));
        System.out.println(Math.round(-1.5));
        System.out.println(Math.round(-1.4));
        System.out.println(Math.round(-11.5));

        System.out.println(Math.round(1.4));
        System.out.println(Math.round(1.5));
        System.out.println(Math.round(1.6));
    }
}

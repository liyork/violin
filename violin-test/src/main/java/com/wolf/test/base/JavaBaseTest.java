package com.wolf.test.base;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * Description:
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

}

package com.wolf.algorithm;

import org.junit.Test;

/**
 * Description:
 *
 * @author 李超
 * @date 2020/01/18
 */
public class OtherOpt {

    // 问题描述：给定字符串，以(开头和)结尾，括号内的元素可以是数字，也可以是括号。去除内部括号并验证表达式是否有误。
    // 分析：从问题描述看此题要实现两个功能：a.判断表达式是否正确；b.消除嵌套的括号。
    // 是否正确：表达式中只有数字、逗号和括号字符，其他字符则非法；其次，判断括号是否匹配。
    @Test
    public void testRemoveNesting() {
//        String s = "(1,(2,3),(4,(5,6),7))";
        String s = "((1,(2,3),(4,(5,6),7))";

        int bracketNum = 0;
        StringBuilder result = new StringBuilder("(");
        char[] chars = s.toCharArray();
        int length = chars.length;

        for (int i = 1; i < length - 1; i++) {
            char tmp = chars[i];
            if (tmp == '(') {
                bracketNum++;
            } else if (tmp == ')') {
                bracketNum--;
                if (bracketNum < 0) {
                    System.out.println("error ..");
                    return;
                }
            } else if ((tmp >= '0' && tmp <= '9') || tmp == ',') {
                result.append(tmp);
            } else {
                System.out.println("error ..");
                return;
            }
        }

        if (bracketNum > 0) {
            System.out.println("error ..");
            return;
        }

        result.append(")");

        System.out.println(result);
    }

    // 不适使用比较运算求两个数的最大值和最小值
    @Test
    public void testMinMax() {

        System.out.println(min(1, 2));
        System.out.println(max(1, 2));
    }

    private int max(int a, int b) {
        return (a + b + Math.abs(a - b)) / 2;
    }

    private int min(int a, int b) {
        return (a + b - Math.abs(a - b)) / 2;
    }
}

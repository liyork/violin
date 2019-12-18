package com.wolf.test.base;

import org.junit.Test;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/12/07
 */
public class ArrayTest {

    // 声明：type[] arrayName
    // 数组被创建后会根据数组存放的数据类型初始化成对应的初始值。
    @Test
    public void testInitArray() {
        int[] a = new int[5];// 创建包含5个整型值的数组，默认初始化为0
        int[] b = {1, 2, 3, 4, 5};// 声明并初始化数组。
        int[] c;// 声明数组类型对象a
        c = new int[5];// 为数组a申请可以存放5个int类型大小的空间，默认值为0
        c = new int[]{1, 2, 3, 4, 5};// 给数组申请存储空间，并初始化值

        int[][] arr = {{1, 2}, {3, 4, 5}};
        int[][] a1 = new int[2][3];
        int[][] a2 = new int[2][];
        a2[0] = new int[]{1, 2};
        a2[1] = new int[]{3, 4, 5};
    }

}

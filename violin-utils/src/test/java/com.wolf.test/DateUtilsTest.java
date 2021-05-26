package com.wolf.test;

import com.wolf.utils.DateUtils;
import org.junit.Test;

/**
 * Description:
 * 命令行中执行：运行当前测试用例在idea中，拷贝里面的命令，然后将空格转义用\，将agent去掉即可
 * Created on 2021/5/26 4:36 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class DateUtilsTest {
    @Test
    public void testForCommand() {
        System.out.println(DateUtils.getCurrentDay());
    }
}

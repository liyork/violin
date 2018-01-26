package com.wolf.test.btrace;

/**
 * Description:
 * <br/> Created on 2018/1/26 16:37
 *
 * @author 李超
 * @since 1.0.0
 */
public class CaseObject {

    private static int sleepTotalTime = 0;

    public boolean execute(int sleepTime) throws Exception {
        System.out.println("sleep: " + sleepTime);
        sleepTotalTime += sleepTime;
        Thread.sleep(sleepTime);
        return true;
    }
}

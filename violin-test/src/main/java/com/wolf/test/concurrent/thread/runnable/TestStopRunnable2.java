package com.wolf.test.concurrent.thread.runnable;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Description:
 * 不论是list.size()或list == null判断，都是两个线程可见性问题，都需要加volatile
 * <p/>
 * Date: 2016/6/23
 * Time: 11:48
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class TestStopRunnable2 implements Runnable {

    private volatile List<Integer> list = new ArrayList<>();
//    private volatile List<Integer> list ;

    public void add(Integer value) {
        list.add(value);
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    @Override
    public void run() {
        System.out.println("====>111");
        while(list.size() != 1) {
//        while(list == null) {
        }
        System.out.println("====>222");
    }
};

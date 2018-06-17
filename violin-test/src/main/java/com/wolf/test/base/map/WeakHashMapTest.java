package com.wolf.test.base.map;

import java.util.WeakHashMap;

/**
 * Description:
 * <br/> Created on 2018/6/12 10:01
 *
 * @author 李超
 * @since 1.0.0
 */
public class WeakHashMapTest {

    //Entry extends WeakReference，创建entry时就是WeakReference.put
    //引用不会被回收，引用的对象有可能被回收
    //当jvm回收entry的key时放入queue，当WeakHashMap的expungeStaleEntries时会进行清理这个key所在的entry
    public static void main(String[] args) {
        WeakHashMap<Integer,String> weakHashMap = new WeakHashMap<Integer,String>();

        int count = 1000000;
        for (int i = 0; i < count; i++) {
            weakHashMap.put(i, "a"+i);
        }

        int size = weakHashMap.size();
        System.out.println("count:"+count+",size:"+size);
    }
}

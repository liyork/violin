package com.wolf.test;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * Description:
 * 每次操作ttl时，在原本放入当前thread内map的基础上又放入thread一个weakmap，作为每个ttl的引用，当需要传递属性时遍历holder中的ttl就可以得到所有相关的value
 * <p>
 * 内部持有holder
 * //持有各种类型的TransmittableThreadLocal,TtlRunnable作为提交与执行task的中介
 * //本以为别thread的set会影响到当前thread的ttl数量，会有多余。它还不是单纯的缓存所有的ttl，而是针对某个线程进行缓存这个线程使用的所有ttl
 * //自己包装的目的应该是:原有的inheritableThreadLocals以及getMAP都不可直接使用。。
 * //要是在runnable中包含原thread呢?也不行。因为threadlocal只能从当前thread中获取。
 * //若是用简单map或者concurrentmap，那么ttl的holder内部将会很大，因为要保留所有线程关联的tl，也有并发问题。
 * <br/> Created on 2018/6/6 9:42
 *
 * @author 李超
 * @since 1.0.0
 */
public class TransmitTLTest {

    public static void main(String[] args) {
        TransmittableThreadLocal transmittableThreadLocal = new TransmittableThreadLocal();
        transmittableThreadLocal.set("a");

        TransmittableThreadLocal transmittableThreadLocal2 = new TransmittableThreadLocal();
        transmittableThreadLocal2.set("b");

        Object o = transmittableThreadLocal.get();
        Object o1 = transmittableThreadLocal2.get();
    }
}

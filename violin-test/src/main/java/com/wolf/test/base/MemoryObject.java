package com.wolf.test.base;

/**
 * Description:
 * <br/> Created on 10/18/17 10:13 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class MemoryObject {

    //1M
    public byte[] placeholder = new byte[2 << 19];

    //会在垃圾回收的标记阶段被调用，不是真正回收阶段，不要依赖不稳定的方法
//    @Override
//    protected void finalize() throws Throwable {
//        System.out.println(Thread.currentThread().getName() + "_the MemoryObject will be gc.");
//    }
}

package com.wolf.test.concurrent.thread;

//同步中代码调用notifyAll不会释放锁，要等后续所有代码执行完退出synchronized或者当前线程调用了wait，释放锁，其他wait线程才能获取锁执行
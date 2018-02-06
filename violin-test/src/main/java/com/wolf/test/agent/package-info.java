package com.wolf.test.agent;

//原始监控代码，业务代码耦合性可能会非常高
//instrumentation 独立于agent 相当于在JVM级别做了AOP支持

//然而通过agentmain的方式在运行时修改class是有限制的,，比如在class已经被加载过的情况下，是不能对class添加，删除方法的
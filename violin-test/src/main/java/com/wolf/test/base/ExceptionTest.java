package com.wolf.test.base;

import org.junit.Test;

/**
 * <p> Description:
 * printStackTrace方法:
 * 打印当前throwable的toString，
 * 获取当前throwable对应的StackTraceElement并打印
 * 执行cause的toString，StackTraceElement(可能有...)，链式调用下个cause
 * <p>
 * 对于testSimpleException
 * 方法栈:top ---> bottom
 * test4 --> testSimpleException
 * 当方法栈顶test4产生异常时，调用当前throwable(“xxx")的fillInStackTrace(0)
 * 方法栈弹出，这时顶部是testSimpleException，无操作，直接弹出
 * 方法栈弹出，这时顶部是com.intellij.rt.execution.application.AppMain.main，调用e.printStackTrace();
 * <p>
 * 对于testCausedBy
 * 方法栈:top ---> bottom
 * test2 --> test1 --> testCausedBy
 * 当方法栈顶test2产生异常时，调用当前throwable("x2")的fillInStackTrace(0)
 * 方法栈弹出，这时顶部是test1，捕获，构造runtime("x1")并放入cause然后fillInStackTrace(0)
 * 方法栈弹出，这时顶部是testCausedBy，捕获，构造runtime("qqq")并放入cause然后fillInStackTrace(0)
 * 方法栈弹出，这时顶部是com.intellij.rt.execution.application.AppMain.main，调用e.printStackTrace();
 * <p/>
 * Date: 2016/5/25
 * Time: 17:35
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ExceptionTest {

	//一个源异常,无Caused by:
//	java.lang.RuntimeException: xxx
//	at com.wolf.test.base.ExceptionTest.test4(ExceptionTest.java:48)
//	at com.wolf.test.base.ExceptionTest.testSimpleException(ExceptionTest.java:43)
	@Test
	public void testSimpleException() {
		System.out.println("1111");
		test4();
	}

	private static void test4() {
		System.out.println("2222");
		throw new RuntimeException("xxx");
	}

	@Test
	public void testCausedBy() {
		System.out.println("main");
		try {
			test1();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("qqq", e);
		}
		//三个源异常，有两个Caused by:
//		java.lang.RuntimeException: qqq
//		at com.wolf.test.base.ExceptionTest.testCausedBy(ExceptionTest.java:57)
//		at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//		at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
//		at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//		at java.lang.reflect.Method.invoke(Method.java:601)
//		at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
//		at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
//		at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
//		at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
//		at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
//		at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
//		at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
//		at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
//		at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
//		at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
//		at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
//		at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
//		at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
//		at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
//		at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:117)
//		at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:42)
//		at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:253)
//		at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:84)
//		at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//		at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
//		at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//		at java.lang.reflect.Method.invoke(Method.java:601)
//		at com.intellij.rt.execution.application.AppMain.main(AppMain.java:147)
//		Caused by: java.lang.RuntimeException: x1
//		at com.wolf.test.base.ExceptionTest.test1(ExceptionTest.java:67)
//		at com.wolf.test.base.ExceptionTest.testCausedBy(ExceptionTest.java:55)
//	... 27 more
//		Caused by: java.lang.RuntimeException: x2
//		at com.wolf.test.base.ExceptionTest.test2(ExceptionTest.java:73)
//		at com.wolf.test.base.ExceptionTest.test1(ExceptionTest.java:65)
//	... 28 more

	}

	private static void test1() {
		System.out.println("1111");
		try {
			test2();
		} catch (Exception e) {
			throw new RuntimeException("x1", e);
		}
	}

	private static void test2() {
		System.out.println("2222");
		throw new RuntimeException("x2");
	}

}

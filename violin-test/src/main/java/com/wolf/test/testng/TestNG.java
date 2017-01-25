package com.wolf.test.testng;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * <p> Description:
 * <p/>
 * Date: 2015/6/24
 * Time: 14:26
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */

public class TestNG {

	@BeforeClass
	public void beforeClass() {
		System.out.println("this is before class");
	}

	@Test
	public void TestNgLearn() {
		System.out.println("this is TestNG test case");
	}

	@AfterClass
	public void afterClass() {
		System.out.println("this is after class");
	}

	@Test(threadPoolSize = 3, invocationCount = 20)
	public void testThread() {
		System.out.println("  " + Thread.currentThread().getId());
	}


}

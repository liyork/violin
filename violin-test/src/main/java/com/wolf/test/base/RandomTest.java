package com.wolf.test.base;

import org.junit.Test;

import java.util.Random;

/**
 * Description:
 * Random.nextInt性能优于(int)(Math.random()*10)
 * 后者有调用开销，调用内部next两次，前者调用next不多于两次
 * <br/> Created on 2016/8/10 17:19
 *
 * @author 李超()
 * @since 1.0.0
 */
public class RandomTest {

	@Test
	public void testMathRandom() {
		for (int i = 0; i < 20; i++) {
			System.out.println((int) (Math.random() * 10));//0-9
		}
	}

	@Test
	public void testRandom() {
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
			System.out.println(random.nextInt(10));
		}
	}

	// 因为在不指定种子的构造函数时系统根据当前时间生成种子，每个种子对应一个数列，
	// 相同的种子会得到相同数列，而不是数值。所以如果在构造函数中指定种子，会得到同一个数列。
	@Test
	public void testRandomSeed() {
		Random random = new Random(20);
		for (int i = 0; i < 20; i++) {
			System.out.println(random.nextInt(10));//生成的数，每次都是按照一定规律3,6,1,1,5,5,3...
		}

		Random random1 = new Random(20);
		Random random2 = new Random(20);
		for (int i = 0; i < 20; i++) {
			//两者生成的一样
			System.out.println("a1:" + random1.nextInt(10));
			System.out.println("a2:" + random2.nextInt(10));
			System.out.println();
		}
	}
}

package com.wolf.behavioral.chain.practical;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p> Description:使用责任链代替if elseif
 * 这里恰巧所有实现方法都相同，就放到一个类里处理操作了。要不会各自实现
 * <p/>
 * Date: 2016/7/19
 * Time: 12:47
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class SwitchScore {

	public static void main(String[] args) {
//		simpleIfElse(50);
//		useForLoop(50);
		useChain(50);
	}

	private static void simpleIfElse(int score) {
		if (score > 80) {
			System.out.println("好学生");
		}else if (score > 60) {
			System.out.println("及格学生");
		}else if (score > 40) {
			System.out.println("还算不错学生");
		}else{
			System.out.println("极差学生");
		}
	}

	private static void useForLoop(int score) {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(80, "好学生");
		map.put(60, "及格学生");
		map.put(40, "还算不错学生");
		map.put(0, "极差学生");

		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			if (score > entry.getKey()) {
				System.out.println(entry.getValue());
				break;
			}
		}
	}

	private static void useChain(int score) {

		FilterChain filterChain1 = new FilterChain(80, "好学生");
		FilterChain filterChain2 = new FilterChain(60, "及格学生");
		FilterChain filterChain3 = new FilterChain(40, "还算不错学生");
		FilterChain filterChain4 = new FilterChain(0, "极差学生");

		filterChain1.setNext(filterChain2);
		filterChain2.setNext(filterChain3);
		filterChain3.setNext(filterChain4);

		System.out.println(filterChain1.handle(score));
	}
}

package com.wolf.algorithm;

import java.util.HashSet;
import java.util.Random;

/**
 * <p> Description: 从指定范围min-max,获取n个随机数
 * <p/>
 * Date: 2015/12/30
 * Time: 12:48
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class RandomNum {

	/**
	 * 随机选取指定范围内n个不重复的数
	 *
	 * 在初始化的无重复待选数组中随机产生一个数放入结果中，
	 * 从len-1数组随机到一个数放入结果集，用待选数组(len-1)下标对应的数替换
	 * 然后从len-2里随机产生下一个随机数，用待选数组(len-2)下标对应的数替换
	 * 如此类推
	 * @param max  指定范围最大值
	 * @param min  指定范围最小值
	 * @param n  随机数个数
	 * @return int[] 随机数结果集
	 */
	public static int[] randomArray(int min,int max,int n){

		int len = max-min+1;

		if(max < min || n > len){
			return null;
		}

		//初始化给定范围的待选数组(min--->max放入数组)
		int[] source = new int[len];
		for (int i = min; i < min+len; i++){
			source[i-min] = i;
		}

		int[] result = new int[n];
		Random rd = new Random();
		int index ;
		for (int i = 0; i < result.length; i++) {
			//待选数组0到(len-1)随机一个下标,然后不断缩小范围
			index = Math.abs(rd.nextInt() % len--);
			//将随机到的数放入结果集
			result[i] = source[index];
			//将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
			source[index] = source[len];
		}
		return result;
	}


	/**
	 * 随机指定范围内N个不重复的数
	 * 最简单最基本的方法
	 * @param min 指定范围最小值
	 * @param max 指定范围最大值
	 * @param n 随机数个数
	 */
	public static int[] randomCommon(int min, int max, int n){

		if (n > (max - min + 1) || max < min) {
			return null;
		}

		int[] result = new int[n];
		int count = 0;
		while(count < n) {
			//(int) (Math.random() * (9-4))+4    [4-8]
							 //(int) ([0,1) * (max - min) ) 得到[0,max-min-1] ,再加min 得到[min,max-1]
							//等同于random.nextInt(max - min)+4
			int num = (int) (Math.random() * (max - min)) + min;
			//查询时候出现过，如有则放弃本次赋值
			boolean flag = true;
			for (int j = 0; j < n; j++) {
				if(num == result[j]){
					flag = false;
					break;
				}
			}
			if(flag){
				result[count] = num;
				count++;
			}
		}
		return result;
	}

	/**
	 * 随机指定范围内N个不重复的数
	 * 利用HashSet的特征，只能存放不同的值
	 * @param min 指定范围最小值
	 * @param max 指定范围最大值
	 * @param n 随机数个数
	 * @param HashSet<Integer> set 随机数结果集
	 */
	public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
		if (n > (max - min + 1) || max < min) {
			return;
		}
		for (int i = 0; i < n; i++) {
			// 调用Math.random()方法
			int num = (int) (Math.random() * (max - min)) + min;
			set.add(num);// 将不同的数存入HashSet中
		}
		int setSize = set.size();
		// 如果存入的数小于指定生成的个数(有重复)，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
		if (setSize < n) {
			randomSet(min, max, n - setSize, set);// 递归
		}
	}

	public static void main(String[] args) {
		int[] reult1 = randomCommon(20,50,10);
		for (int i : reult1) {
			System.out.println(i);
		}

		int[] reult2 = randomArray(20,50,10);
		for (int i : reult2) {
			System.out.println(i);
		}

		HashSet<Integer> set = new HashSet<Integer>();
		randomSet(20,50,10,set);
		for (int j : set) {
			System.out.println(j);
		}
	}

}

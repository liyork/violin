package com.wolf.algorithm;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.*;

/**
 * <p> Description:桶排序，先将各元素划分到桶中，然后将同种元素排序，分开排序，最后顺序输出数组
 * 时间复杂度：O(n)
 * 假设输入的待排序元素是等可能的落在等间隔的值区间内
 * <p/>
 * Date: 2015/12/24
 * Time: 20:13
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class BucketSort {

	public static void main(String[] args) {
		double array[] = {0.78, 0.17, 0.39, 0.26, 0.72, 0.94, 0.21, 0.12, 0.23, 0.68};
		bucketSort(array);
		for (double anArray : array) {
			System.out.print(anArray + " ");
		}
	}

	private static void bucketSort(double[] array) {

		//定义11个元素，下标从0-10
		Map<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();

		for (Double v : array) {
			int decimal = (int) (v * 10);
			if (null == map.get(decimal)) {
				map.put(decimal, new ArrayList<Double>());
			}
			map.get(decimal).add(v);
		}

		int count = 0;
		for (Map.Entry<Integer, List<Double>> entry : map.entrySet()) {
			List<Double> element = entry.getValue();
			if (CollectionUtils.isNotEmpty(element)) {
				Collections.sort(element);
				for (Double aDouble : element) {
					array[count] = aDouble;
					count++;
				}
			}
		}
	}

	//hashmap会根据key生成hashcode放入内部数组中，不能保证放入时的顺序
	@Test
	public void testHashMapOrder(){
		//定义11个元素，下标从0-10
		Map<String, String> map = new HashMap<String, String>();
		map.put("a2", "a");
		map.put("a1", "b");
		map.put("a3", "b");

		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey());
		}
	}
}

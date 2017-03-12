package com.wolf.test.fund;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Description:
 * <br/> Created on 2017/3/12 17:18
 *
 * @author 李超(lichao07@zuche.com)
 * @since 1.0.0
 */
public class FundTest {

	public static void main(String argv[]) {
		String fivePath = "D:\\fund\\fiveyear.txt";
		Map<String, String> fiveMap = new HashMap<String, String>();
		readFile(fivePath, fiveMap);

		String threePath = "D:\\fund\\threeyear.txt";
		Map<String, String> threeMap = new HashMap<String, String>();
		readFile(threePath, threeMap);

		String twoPath = "D:\\fund\\twoyear.txt";
		Map<String, String> twoMap = new HashMap<String, String>();
		readFile(twoPath, twoMap);

		String onePath = "D:\\fund\\oneyear.txt";
		Map<String, String> oneMap = new HashMap<String, String>();
		readFile(onePath, oneMap);
		System.out.println(fiveMap.size());
		Iterator<String> iterator = fiveMap.keySet().iterator();
		while (iterator.hasNext()) {
			String s1 = iterator.next();
			if (!threeMap.containsKey(s1)) {
				iterator.remove();
			}
		}
		iterator = fiveMap.keySet().iterator();
		while (iterator.hasNext()) {
			String s1 = iterator.next();
			if (!twoMap.containsKey(s1)) {
				iterator.remove();
			}
		}
		iterator = fiveMap.keySet().iterator();
		while (iterator.hasNext()) {
			String s1 = iterator.next();
			if (!oneMap.containsKey(s1)) {
				iterator.remove();
			}
		}
		iterator = fiveMap.keySet().iterator();
		while (iterator.hasNext()) {
			String s1 = iterator.next();
			System.out.println(s1);
		}
		System.out.println(fiveMap.size());

	}

	private static void readFile(String filePath, Map<String, String> fiveMap) {
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { //判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					String $1 = lineTxt.replaceAll("[^\\u4e00-\\u9fa5]", "");
					fiveMap.put($1, $1);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
	}
}

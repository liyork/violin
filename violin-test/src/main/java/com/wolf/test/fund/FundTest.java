package com.wolf.test.fund;

import com.wolf.utils.PathGettingUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description:
 * <br/> Created on 2017/3/12 17:18
 *
 * @author 李超
 * @since 1.0.0
 */
public class FundTest {

    public static void main(String argv[]) {
//        performance("stock");
//        performance("mixture");
        performance("index");
    }

    private static void performance(String type) {
        String fivePath = "fund/" + type + "/fiveyear.txt";
        Map<String, String> fiveMap = new LinkedHashMap<>();
        readFile(fivePath, fiveMap);

        String threePath = "fund/" + type + "/threeyear.txt";
        Map<String, String> threeMap = new LinkedHashMap<String, String>();
        readFile(threePath, threeMap);

        String twoPath = "fund/" + type + "/twoyear.txt";
        Map<String, String> twoMap = new LinkedHashMap<String, String>();
        readFile(twoPath, twoMap);

        String onePath = "fund/" + type + "/oneyear.txt";
        Map<String, String> oneMap = new LinkedHashMap<String, String>();
        readFile(onePath, oneMap);
        System.out.println(fiveMap.size());
        Iterator<String> iterator = fiveMap.keySet().iterator();
        while(iterator.hasNext()) {
            String s1 = iterator.next();
            if(!threeMap.containsKey(s1)) {
                iterator.remove();
            }
        }
        iterator = fiveMap.keySet().iterator();
        while(iterator.hasNext()) {
            String s1 = iterator.next();
            if(!twoMap.containsKey(s1)) {
                iterator.remove();
            }
        }
        iterator = fiveMap.keySet().iterator();
        while(iterator.hasNext()) {
            String s1 = iterator.next();
            if(!oneMap.containsKey(s1)) {
                iterator.remove();
            }
        }

        for(Map.Entry<String, String> entry : fiveMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println(fiveMap.size());
    }

    private static void readFile(String filePath, Map<String, String> fiveMap) {
        try {
            String encoding = "UTF-8";
            URL resource = PathGettingUtils.getResourceFromClassPath(FundTest.class, filePath);
            System.out.println(resource);
            InputStreamReader read = new InputStreamReader(new FileInputStream(resource.getFile()), encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while((lineTxt = bufferedReader.readLine()) != null) {
                String string = lineTxt.replaceAll("(.*(\\d{6})(\\t[\\u4E00-\\u9FA5].*?\\t).*)", "$2 $3");
                String[] split = string.split(" ");
                fiveMap.put(split[0], split[1]);
            }
            read.close();

        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }
}

package com.wolf.test.fund;

import com.wolf.utils.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
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
        String fivePath = "fund/fiveyear.txt";
        Map<String, String> fiveMap = new HashMap<String, String>();
        readFile(fivePath, fiveMap);

        String threePath = "fund/threeyear.txt";
        Map<String, String> threeMap = new HashMap<String, String>();
        readFile(threePath, threeMap);

        String twoPath = "fund/twoyear.txt";
        Map<String, String> twoMap = new HashMap<String, String>();
        readFile(twoPath, twoMap);

        String onePath = "fund/oneyear.txt";
        Map<String, String> oneMap = new HashMap<String, String>();
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
        iterator = fiveMap.keySet().iterator();
        while(iterator.hasNext()) {
            String s1 = iterator.next();
            System.out.println(s1);
        }
        System.out.println(fiveMap.size());

    }

    private static void readFile(String filePath, Map<String, String> fiveMap) {
        try {
            String encoding = "UTF-8";
            URL resource = ResourceUtils.getResourceFromClassPath(FundTest.class, filePath);
            System.out.println(resource);
            InputStreamReader read = new InputStreamReader(new FileInputStream(resource.getFile()), encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while((lineTxt = bufferedReader.readLine()) != null) {
                String $1 = lineTxt.replaceAll("(.*(\\d{6}).*)", "$2");
                fiveMap.put($1, $1);
            }
            read.close();

        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }
}

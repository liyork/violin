package com.wolf.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description:
 * <br/> Created on 1/19/18 9:56 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class Test {

    public static void main(String[] args) {

        Map<String, List<String>> adjWords = new TreeMap<>();

        List<String> groupsWords = new ArrayList<>();
        groupsWords.add("abc");
        groupsWords.add("aqc");
        groupsWords.add("axc");
        groupsWords.add("bbc");
        groupsWords.add("bxc");
        groupsWords.add("bac");
        groupsWords.add("bbc");
        int groupNum = 3;

        for (int i = 0; i < groupNum; i++) {
            Map<String, List<String>> repToWord = new TreeMap<>();
            for (String str : groupsWords) {
                String rep = str.substring(0, i) + str.substring(i + 1);
                update(repToWord,rep,str);
            }

            for (List<String> wordClique : repToWord.values()) {
                if (wordClique.size() >= 2) {
                    for (String s1 : wordClique) {
                        for (String s2 : wordClique) {
                            if (!s1.equals(s2)) {
                                update(adjWords,s1,s2);
                            }
                        }
                    }
                }
            }
        }

        for (Map.Entry<String, List<String>> entry : adjWords.entrySet()) {
            System.out.println("key:"+entry.getKey()+" value:"+entry.getValue());
        }
    }

    private static void update(Map<String, List<String>> m, String key, String value) {
        List<String> lst = m.get(key);
        if (lst == null) {
            lst = new ArrayList<>();
            m.put(key, lst);
        }
        lst.add(value);
    }
}

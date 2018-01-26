package com.wolf.test.writeexam;

import com.alibaba.fastjson.JSON;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * Description:
 * <br/> Created on 2017/5/2 17:30
 *
 * @author 李超
 * @since 1.0.0
 */
public class TreeSetTest {

    public static void main(String[] args) {
        //用来比较的,hashMap是用key进行排序
        Comparator<User> comparator = new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                Integer age1 = o1.age;
                Integer age2 = o2.age;
                if(age1 < age2) {
                    return -1;
                } else if(age1 > age2) {
                    return 1;
                }else{
                    return o1.name.compareTo(o2.name);
                }
            }
        };
        TreeSet<User> treeSet = new TreeSet<User>(comparator);
        treeSet.add(new User("张三", 5));
        treeSet.add(new User("李四", 3));
        treeSet.add(new User("王五4", 4));
        treeSet.add(new User("王五2", 2));
        treeSet.add(new User("王五8", 8));
        treeSet.add(new User("王五7", 7));
        treeSet.add(new User("王五9", 9));

        for(User user : treeSet) {
            System.out.println(JSON.toJSONString(user));
        }

    }

    static class User{
        String name;
        Integer age;

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }
    }
}

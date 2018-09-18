package com.wolf.utils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Description:数组工具类
 * <br/> Created on 18/09/2018 8:59 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class ArrayUtils {

//    public static <T> ArrayList<T> toList(T[] arr) {
//        ArrayList<T> result = new ArrayList<>(arr.length);
//        Collections.addAll(result, arr);
//        return result;
//    }

    //已有的Arrays.asList返回的不是ArrayList，而是一个内部类。而且不支持add和remove。size大小固定
    //本方法提供想要真正一个ArrayList
    public static <T> ArrayList<T> toList(T ...arr) {
        ArrayList<T> result = new ArrayList<>(arr.length);
        Collections.addAll(result, arr);
        return result;
    }

    public static <T> ArrayList<T> toList(T arr) {
        ArrayList<T> result = new ArrayList<>(1);
        Collections.addAll(result, arr);
        return result;
    }

    //数组是否包含，不要arr->list->hashset->contains那样麻烦。
    public static <T> boolean contains(T s ,T[] arr) {

        for (T t : arr) {
            if (s.equals(t)) {
                return true;
            }
        }
        return false;
    }
}

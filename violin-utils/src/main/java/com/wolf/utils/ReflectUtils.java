package com.wolf.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description:
 * <br/> Created on 2018/5/31 16:46
 *
 * @author 李超
 * @since 1.0.0
 */
public class ReflectUtils {

    //递归向上获取字段
    public static Field getField(Class clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superclass = clazz.getSuperclass();
            if (superclass != null) {
                return getField(superclass, fieldName);
            }
        }
        return field;
    }

    public static Collection<Field> getAllFields(Class clazz) {

        Map<String, Field> resutlMap = new LinkedHashMap<String, Field>();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                resutlMap.put(field.getName(), field);
            }
        }
        return resutlMap.values();
    }
}

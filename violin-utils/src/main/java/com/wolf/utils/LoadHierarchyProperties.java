/**
 * Description: LoadHierarchyProperties.java
 * All Rights Reserved.
 *

 */
package com.wolf.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 *  加载带有层级的properties 工具类
 *  非线程安全
 */
public final class LoadHierarchyProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadHierarchyProperties.class);

    /**
     * 用来标识属性是否为列表形式
     */
    private static final String IS_LIST_TAG = "#";

    private LoadHierarchyProperties() {
    }


    /**
     * 加载方法
     *
     * <br/> Created on 2013-10-9 下午3:34:20

     * @since 3.2
     * @param propertiesName，加载properties 名称
     * @return
     */
    public static List<Object> loadHierarchy(String propertiesName){
        Properties ps;
        try{
            ps = PropertiesReader.getProperties(propertiesName);
        }catch (Exception e) {
            LOGGER.error("加载层级properties 失败！", e);
            throw new RuntimeException("加载层级properties 失败！", e);
        }
        if(ps == null){
            return null;
        }
        return  loadHierarchy(ps);
    }

    /**
     * 加载方法
     *
     * <br/> Created on 2013-10-9 下午3:34:20

     * @since 3.2
     * @return
     */
	public static List<Object> loadHierarchy(Properties ps){

        try {
            List<String> list = new ArrayList<String>();
            Enumeration<Object> enu = ps.keys();
            while(enu.hasMoreElements()) {
                String obj = String.valueOf(enu.nextElement());
                list.add(obj);
            }
            Collections.sort(list);
            String head = null;
            List<String> subList = new ArrayList<String>();
            List<Object> listResult = new ArrayList<Object>();
            for(int i = 0; i < list.size(); i++) {
                String value = list.get(i);
                if(head == null) {
                    head = value.substring(0, value.indexOf(".") + 1);
                }
                if(value.startsWith(head)) {
                    subList.add(value);
                } else {
                    Object obj = setVO(subList, ps, null, null, 0, new Count());
                    listResult.add(obj);
                    subList.clear();
                    head = value.substring(0, value.indexOf(".") + 1);
                    subList.add(value);
                }
                if(i + 1 == list.size()) {
                    Object obj = setVO(subList, ps, null, null, 0, new Count());
                    listResult.add(obj);
                    subList.clear();
                }

            }
            return listResult;
        } catch (Exception e) {
            LOGGER.error("加载层级properties 失败！", e);
            throw new RuntimeException("加载层级properties 失败！", e);
        }

    }

    private static Object setVO(List<String> list, Properties ps, Object objParam, String parentParam, int count, Count con) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException {
        String parent = parentParam;
        Object obj = objParam;
        for(int i = count; i < list.size(); i++) {
            String key = list.get(i);
            String value = ps.getProperty(key);
            String fie = key.substring(key.lastIndexOf(".") + 1, key.length()).trim();
            int isListIndex = -1;

            //用来标识该属性是否为列表
            boolean isList = false;
            int isListTagIndex = fie.indexOf(IS_LIST_TAG);
            if(isListTagIndex != -1) {
                isList = true;
                isListIndex = Integer.valueOf(fie.substring(isListTagIndex + 1, fie.length()));
                fie = fie.substring(0, isListTagIndex);
            }

            if(obj != null && !key.startsWith(parent)) {
                return null;
            }
            try {
                Class<?> clazz = Class.forName(value);
                if(obj == null) {
                    obj = clazz.newInstance();
                    parent = key;
                } else {
                    Field field = obj.getClass().getDeclaredField(fie);
                    if(!Modifier.isPublic(field.getModifiers())) {
                        field.setAccessible(true);
                    }

                    //属性值的内容
                    Object newObj = clazz.newInstance();
                    con.addOne();
                    //列表形式的属性
                    if(isList) {
                        List propertyList = (List) field.get(obj);
                        propertyList.add(isListIndex, newObj);
                    }
                    //非列表形式的属性
                    else {
                        field.set(obj, newObj);
                        if(i + 1 < list.size()) {
                            Count c = new Count();
                            setVO(list, ps, newObj, key, i + 1, c);
                            i = i + c.getCountCut();
                        }
                    }


                    continue;
                }

            } catch (ClassNotFoundException e) {
                Field field = ClassUtils.getAssignField(obj.getClass(), fie);
                if(field == null) {
                    throw new RuntimeException(obj + "没有对应的属性,解析失败！");
                }

                if(!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                Class<?> type = field.getType();

                field.set(obj, changeType(type, value));
                con.addOne();
            }
        }
        return obj;

    }

    private static Object changeType(Class<?> type, String value) {

        if(type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
            return Integer.parseInt(value);
        }

        if(type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class)) {
            return Boolean.parseBoolean(value);
        }
        //long 十进制 最大值 9223372036854775807
        if(type.isAssignableFrom(Long.class) || type.isAssignableFrom(long.class)) {
            return Long.parseLong(value);
        }

        if(type.isAssignableFrom(double.class) || type.isAssignableFrom(Double.class)) {
            return Double.parseDouble(value);
        }

        return value;
    }

    static class Count {

        private int countCut = 0;

        public int getCountCut() {
            return countCut;
        }

        public void addOne() {
            countCut++;
        }

    }

}

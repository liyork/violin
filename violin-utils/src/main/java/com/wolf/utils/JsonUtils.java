package com.wolf.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Map;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/29
 * Time: 15:14
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class JsonUtils {

    public static String toJsonString(Object object) {
        return JSON.toJSONString(object);
    }

    public static String toJsonString(Object data, SerializerFeature ...serializerFeature) {
        return JSON.toJSONString(data, serializerFeature);
    }

    public static String toJsonString(Object data, String dateFormat, SerializerFeature serializerFeature) {
        return JSON.toJSONStringWithDateFormat(data, dateFormat, serializerFeature);
    }

    public static Map<String, String> toMap(String string) {
        return JSON.parseObject(string, new TypeReference<Map<String, String>>() {
        });
    }

    public static <T> T toPointType(String string, TypeReference<T> typeReference) {
        return JSON.parseObject(string, typeReference);
    }

    public static JSONObject toJSONObject(String string) {
        return JSON.parseObject(string);
    }

    public static <T> T toBean(String string, Class<T> clazz) {
        return JSON.parseObject(string, clazz);
    }
}

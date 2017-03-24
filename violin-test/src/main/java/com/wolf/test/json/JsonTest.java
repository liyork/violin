package com.wolf.test.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wolf.utils.JsonUtils;
import org.junit.Test;

import java.util.*;

/**
 * <p> Description:
 * JSONArray  实现 list ["爬山","骑车","旅游"]
 * JSONObject 实现 map  {"age":"23","name":"xiaohong"}
 * <p/>
 * Date: 2016/6/29
 * Time: 15:16
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class JsonTest {

    public static void main(String[] args) {
//		test2();
//		test3();
//		test4();
//		test5();
//		testJsonArrayAndObject();
//		testToBeanUsingInterfaceOrClass();

    }

    @Test
    public void testObjectToJsonString() {
        UserInfo info = new UserInfo();
        info.setName("zhangsan");
        info.setAge(24);
        //将对象转换为JSON字符串
        String str_json = JsonUtils.toJsonString(info);
        System.out.println("JSON=" + str_json);
    }

    @Test
    public void testToString() {

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("username", "zhangsan");
        map.put("age", 24);
        map.put("sex", "男");

        //map集合
        HashMap<String, Object> temp = new HashMap<String, Object>();
        temp.put("name", "xiaohong");
        temp.put("age", "23");
        map.put("girlInfo", temp);

        //list集合
        List<String> list = new ArrayList<String>();
        list.add("爬山");
        list.add("骑车");
        list.add("旅游");
        map.put("hobby", list);

        String jsonString = JsonUtils.toJsonString(map);
        System.out.println("JSON=" + jsonString);
    }

    @Test
    public void testToBean() {

        String json = "{\"name\":\"chenggang\",\"age\":24}";
        //反序列化
        UserInfo userInfo = JsonUtils.toBean(json, UserInfo.class);
        System.out.println("name:" + userInfo.getName() + ", age:" + userInfo.getAge());

    }

    /**
     * 泛型的反序列化
     */
    @Test
    public void testToPointType() {
        String json = "{\"user\":{\"name\":\"zhangsan\",\"age\":25}}";
        Map<String, UserInfo> map = JsonUtils.toPointType(json, new TypeReference<Map<String, UserInfo>>() {
        });
        System.out.println(map.get("user"));
    }

    @Test
    public void testSerializerFeature() {

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("username", "zhangsan");
        map.put("age", new Date());
        map.put("sex", "男");

        System.out.println(JsonUtils.toJsonString(map));
        //默认格式为yyyy-MM-dd HH:mm:ss
        System.out.println(JsonUtils.toJsonString(map, SerializerFeature.WriteDateUseDateFormat));
        //根据自定义格式输出日期
        System.out.println(JsonUtils.toJsonString(map, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));
        //单引号
        String jsonString1 = JsonUtils.toJsonString(map, SerializerFeature.UseSingleQuotes);
        System.out.println("JSON=" + jsonString1);

    }

    @Test
    public void testJsonArrayAndObject() {
        String jsonString = "{\"age\":24,\"girlInfo\":{\"age\":\"23\",\"name\":\"xiaohong\"},\"hobby\":[\"爬山\",\"骑车\",\"旅游\"],\"sex\":\"男\",\"username\":\"zhangsan\"}";
        JSONObject jsonObject = JsonUtils.toJSONObject(jsonString);
        Object hobby = jsonObject.get("hobby");
        boolean b1 = hobby instanceof JSONArray;
        System.out.println("hobby instanceof JSONArray==>" + b1);
        Object girlInfo = jsonObject.get("girlInfo");
        boolean b2 = girlInfo instanceof JSONObject;
        System.out.println("girlInfo instanceof JSONObject==>" + b2);
    }

    //如果使用接口作为返回值类型，则JSON会使用动态代理，代理JSONObject，(JSONObject实现InvocationHandler)
    //如果使用类，则JSON会生成对应的实体bean对象，类似于new出来一样
    @Test
    public void testToBeanUsingInterfaceOrClass() {

        String jsonString = "{\"age\":24,\"name\":\"zhangsan\"}";
        UserInfoInterface userInfoInterface = JsonUtils.toBean(jsonString, UserInfoInterface.class);
        System.out.println(userInfoInterface.getName());
        userInfoInterface.setName("111");
        System.out.println(userInfoInterface);
        UserInfo userInfo = JsonUtils.toBean(jsonString, UserInfo.class);
        System.out.println(userInfo.getName());
    }

    @Test
    public void testJsonConfig() {

        SerializerFeature[] jsonConfig = {SerializerFeature.WriteMapNullValue,//字段为null按照下面3个指定的规则输出,默认：不输出null字段，空集合输出[]
        SerializerFeature.WriteNullListAsEmpty,//空的list或者null，输出[],默认：空的list输出[],null就输出null
        SerializerFeature.WriteNullNumberAsZero,//数值包装类如果为null,输出为0,默认：null
        SerializerFeature.WriteNullStringAsEmpty,//字符类型如果为null,输出为”“,默认：null
        SerializerFeature.WriteNullBooleanAsFalse//boolean为null时输出false
        };

        ObjectForTest objectForTest = new ObjectForTest();
        String jsonString = JsonUtils.toJsonString(objectForTest, jsonConfig);
        System.out.println(jsonString);
    }

    /**
     * 使用自定义格式
     * 对于42(包含)以后的版本
     * 其实SerializerFeature.WriteEnumUsingToString没有任何意义的。而且fastjson的大部分代码中，
     * 对于SerializerFeature.WriteEnumUsingToString的处理是用的枚举的name()。
     * 但是在com.alibaba.fastjson.serializer.EnumSerializer中对于启用SerializerFeature.WriteEnumUsingToString特性，
     * 却使用了枚举的toString方法。而且对于自定义toString的枚举根本没有办法反序列化。
     */
    @Test
    public void testCustomizedJsonConfig() {

        ObjectForTest objectForTest = new ObjectForTest();

        String s = JSON.toJSONStringZ(objectForTest, SerializeConfig.getGlobalInstance(), SerializerFeature.WriteMapNullValue, SerializerFeature.QuoteFieldNames, SerializerFeature.UseSingleQuotes,
        //默认是带这个的，使用name(),如果去掉则使用ordinal()
        SerializerFeature.WriteEnumUsingToString, SerializerFeature.SkipTransientField);
        System.out.println(s);
    }

    @Test
    public void testAppendSerializerFeature() {

        Date date = new Date(1308841916550L);
        System.out.println(JsonUtils.toJsonString(date)); // 1308841916550
        System.out.println(JsonUtils.toJsonString(date, SerializerFeature.UseISO8601DateFormat));
        SerializerFeature[] features = {SerializerFeature.UseISO8601DateFormat, SerializerFeature.UseSingleQuotes};
        System.out.println(JsonUtils.toJsonString(date, features)); // '2011-06-23T23:11:56.550'
    }

    //===============测试SerializerFeature内部原理，需要查看里面的of和isEnabled方法

    //当初错误的以为内部是这样的逻辑，但是源码看错了，应该是1 << ordinal()，位置反过来了不是ordinal() << 1
    //&操作，两个位置都是1才为1，
    //|操作，两个位置有一个1就为1，
    @Test
    public void testErrorSource() {
        int ordinal1 = SerializerFeature.QuoteFieldNames.ordinal();
        int ordinal2 = SerializerFeature.WriteMapNullValue.ordinal();
        int ordinal3 = SerializerFeature.WriteEnumUsingToString.ordinal();
        int ordinal4 = SerializerFeature.UseISO8601DateFormat.ordinal();
        System.out.println(ordinal1 + "_" + ordinal2 + "_" + ordinal3 + "_" + ordinal4);
        System.out.println(ordinal1 << 1);
        int x = ordinal2 << 1;
        System.out.println("x==>" + x);
        int y = ordinal3 << 1;
        System.out.println("y==>" + y);
        int z = x | y;
        System.out.println("z=(x|y)==>" + z);

        System.out.println("x&z==>" + (x & z));
        System.out.println("y&z==>" + (y & z));
        int q = ordinal4 << 1;
        System.out.println("q==>" + q);
        System.out.println("q&z==>" + (q & z));
        System.out.println((2 | 4 | 6 | 8 | 10 | 12) & 14);//不能达到判断是否存在的目的，因为计算结果与14本身重复了。
        System.out.println((1 | 2 | 4) & 13);

    }

    /**
     * 正确的内部原理
     * 执行对于2的所有指数，执行|只能把对应的二进制中的1都累加起来，然后当执行&时，如果在这里面当然结果
     * 还是他自己，但是如果不在，表明这个1的位置肯定不在结果里面，所以会返回0
     * |两个中有一个是1结果为1
     * &两个必须都为1
     */
    @Test
    public void testCorrectSource() {
        int ordinal1 = SerializerFeature.QuoteFieldNames.ordinal();
        int ordinal2 = SerializerFeature.WriteMapNullValue.ordinal();
        int ordinal3 = SerializerFeature.WriteEnumUsingToString.ordinal();
        int ordinal4 = SerializerFeature.UseISO8601DateFormat.ordinal();
        System.out.println(ordinal1 + "_" + ordinal2 + "_" + ordinal3);
        System.out.println(1 << ordinal1);//2^0
        int x = 1 << ordinal2;
        System.out.println("x==>" + x);
        int y = 1 << ordinal3;
        System.out.println("y==>" + y);
        int z = x | y;
        System.out.println("(z=x|y)==>" + z);

        System.out.println("x&z==>" + (x & z));
        System.out.println("y&z==>" + (y & z));
        int q = 1 << ordinal4;
        System.out.println("q==>" + q);
        System.out.println("q&z==>" + (q & z));
        System.out.println((2 | 4 | 8 | 16 | 32) & 64);//不存在
        System.out.println((1 | 2 | 4) & 2);//存在

        //basetest
        System.out.println("3 | 2==>" + (3 | 2));
        System.out.println("3 & 2==>" + (3 & 2));

    }


    @Test
    public void testJsonObject() {
        Map<String, Object> q = new HashMap<String, Object>();

        q.put("carTypeId", 3);
        q.put("lon", "117.39344");
        q.put("lat", "39.171112");

        //错误方式
        InnerClass innerClass = new InnerClass();
        innerClass.setDistrictName("红桥区");
        innerClass.setCityName("天津市");
        //这样的字符串中带有转译的""
        String s4 = JSONObject.toJSONString(innerClass);
        q.put("innerClass", s4);

        //正确方式
//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.put("cityName", "天津市");
//        jsonObject1.put("districtName", "红桥区");
//        q.put("innerClass", jsonObject1);

        String value = JSONObject.toJSON(q).toString();
        System.out.println("value=" + value);

        JSONObject jsonObject = JSONObject.parseObject(value);
        System.out.println("jsonObject=" + value);

        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.putAll(jsonObject);

        String jsonParam = JSONObject.toJSONString(map4);
        System.out.println("jsonParam=" + value);
        //这里的字符串中，如果是对象不能这样aa:"{，要这样aa:{
        OuterClass outerClass = JSONObject.parseObject(jsonParam, OuterClass.class);
        System.out.println(JSON.toJSONString(outerClass));

    }

}

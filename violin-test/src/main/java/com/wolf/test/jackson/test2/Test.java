package com.wolf.test.jackson.test2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Description:
 * Created on 2021/3/31 9:26 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class Test {
    private static XmlMapper xmlMapper = new XmlMapper();

    public static void main(String[] args) throws IOException {
        Root root = new Root();

        Element1 element1 = new Element1();

        Element2 element2 = new Element2();
        element2.setValue("A String");

        element1.setElement2(element2);
        root.setElement1(element1);

        XmlMapper xmlMapper = getXmlMapper();
        //xml.writeValue(System.out, bean);
        StringWriter sw = new StringWriter();
        xmlMapper.writeValue(sw, root);
        System.out.println(sw.toString());
    }

    private static XmlMapper getXmlMapper() {
        xmlMapper.setDefaultUseWrapper(false);
        //字段为null，自动忽略，不再序列化
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //XML标签名:使用骆驼命名的属性名，
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        //设置转换模式
        xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
        return xmlMapper;
    }
}

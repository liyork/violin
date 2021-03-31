package com.wolf.test.jackson.base;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Description:Jackson是一个处理JSON的类库，不过它也通过jackson-dataformat-xml包提供了处理XML的功能。
 * Created on 2021/3/31 9:40 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class JacksonTest {
    public static void main(String[] args) throws IOException {
        String xml = beanToXml();
        System.out.println(xml);

        xmlToBean(xml);
    }

    public static String beanToXml() throws IOException {
        RootXml.Pub pub = new RootXml.Pub();
        pub.setTransCode("transCode");
        pub.setBankCode("bankCode");
        pub.setId("id");
        RootXml.In in = new RootXml.In();
        in.setAccountNo("account");
        in.setAreaCode("areaCode");
        RootXml.Eb eb = new RootXml.Eb();
        eb.setIn(in);
        eb.setPub(pub);
        RootXml rootXml = new RootXml();
        rootXml.setEb(eb);

        XmlMapper xmlMapper = new XmlMapper();
        //xml.writeValue(System.out, bean);
        StringWriter sw = new StringWriter();
        xmlMapper.writeValue(sw, rootXml);
        return sw.toString();
    }

    public static void xmlToBean(String xml) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            RootXml rootXml = xmlMapper.readValue(xml, RootXml.class);
            System.out.println();
            System.out.println(rootXml.toString());
        } catch (Exception e) {
            System.out.println("xml转Bean时发生错误:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

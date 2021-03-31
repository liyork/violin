package com.wolf.test.jackson.test3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.util.Arrays;

/**
 * Description:
 * Created on 2021/3/31 9:59 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setDefaultUseWrapper(false);
        //字段为null，自动忽略，不再序列化
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //XML标签名:使用骆驼命名的属性名，
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        //设置转换模式
        xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);

        //序列化 bean--->xml
        Group group = new Group();

        Teacher teacher = new Teacher();
        teacher.setTeacherTypeCode(new TeacherType("0", "t1value"));
        teacher.setName("t1name");
        teacher.setAge("25");
        teacher.setGender("1");

        Student student1 = new Student();
        student1.setId("001");
        student1.setName("s1name");
        student1.setAge("18");
        student1.setGender("1");

        Student student2 = new Student();
        student2.setId("002");
        student2.setName("s2name");
        student2.setAge("18");
        student2.setGender("1");

        Student student3 = new Student();
        student3.setId("003");
        student3.setName("s3name");
        student3.setAge("18");
        student3.setGender("0");

        group.setTeacher(teacher);
        group.setStudent(Arrays.asList(student1, student2, student3));

        String result = xmlMapper.writeValueAsString(group);
        System.out.println("序列化结果：" + result);

    }
}

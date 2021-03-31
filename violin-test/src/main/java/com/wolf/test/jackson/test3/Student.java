package com.wolf.test.jackson.test3;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Description:
 * Created on 2021/3/31 9:59 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class Student {

    @JacksonXmlProperty(isAttribute = true,localName = "stu_id")
    private String id;  //学号---属性
    private String name;
    private String gender;
    private String age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

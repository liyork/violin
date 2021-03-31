package com.wolf.test.jackson.test3;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

/**
 * Description:
 * Created on 2021/3/31 9:59 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class TeacherType {
    @JacksonXmlProperty(isAttribute = true, localName = "type")
    private String type;

    @JacksonXmlText
    private String value;

    public TeacherType() {

    }

    public TeacherType(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.wolf.test.jackson.test2;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

/**
 * Description:
 * Created on 2021/3/31 9:25 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class Element2 {
    @JacksonXmlProperty(isAttribute = true)
    private String ns = "yyy";
    @JacksonXmlText// <Value>A String</Value>变成A String
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}


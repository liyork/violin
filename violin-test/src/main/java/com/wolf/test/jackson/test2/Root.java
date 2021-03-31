package com.wolf.test.jackson.test2;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Description:
 * Created on 2021/3/31 9:25 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class Root {
    @JacksonXmlProperty(localName = "Element1")
    private Element1 element1;

    public Element1 getElement1() {
        return element1;
    }

    public void setElement1(Element1 element1) {
        this.element1 = element1;
    }
}

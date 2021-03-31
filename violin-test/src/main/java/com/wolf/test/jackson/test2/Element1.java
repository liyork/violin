package com.wolf.test.jackson.test2;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Description:
 * Created on 2021/3/31 9:25 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class Element1 {
    @JacksonXmlProperty(isAttribute = true)
    private String ns = "xxx";
    @JacksonXmlProperty(localName = "Element2")
    private Element2 element2;

    public Element2 getElement2() {
        return element2;
    }

    public void setElement2(Element2 element2) {
        this.element2 = element2;
    }
}


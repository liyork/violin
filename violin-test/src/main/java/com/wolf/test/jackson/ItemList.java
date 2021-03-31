package com.wolf.test.jackson;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

/**
 * Description:
 * Created on 2021/3/29 1:50 PM
 *
 * @author 李超
 * @version 0.0.1
 */
@JacksonXmlRootElement(localName = "itemList")
public class ItemList {
    @JacksonXmlProperty(isAttribute = true)
    private String attr;
    @JacksonXmlElementWrapper(useWrapping = false)// 可以将xml中的子元素添加到本对象的list中
    @JacksonXmlProperty(localName = "item")
    private List<Item> items;

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

class Item {
    @JacksonXmlProperty(isAttribute = true)
    private String itemName;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}

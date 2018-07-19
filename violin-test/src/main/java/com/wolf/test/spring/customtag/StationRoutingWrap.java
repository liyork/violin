package com.wolf.test.spring.customtag;

/**
 * Description:
 * <br/> Created on 2018/6/28 12:15
 *
 * @author 李超
 * @since 1.0.0
 */
public class StationRoutingWrap {

    private String id;

    private String name;

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

    @Override
    public String toString() {
        return "StationRoutingWrap{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

package com.wolf.test.jibx;

import java.util.List;

/**
 * Description:
 * <br/> Created on 2018/2/6 9:53
 *
 * @author 李超
 * @since 1.0.0
 */
public class ListBean {

    private String name;
    private List<Account> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getList() {
        return list;
    }

    public void setList(List<Account> list) {
        this.list = list;
    }
}

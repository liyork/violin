package com.wolf.utils.redis;

/**
 * Description:
 * <br/> Created on 2017/4/13 14:32
 *
 * @author 李超
 * @since 1.0.0
 */
public class Node {

    private String name;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

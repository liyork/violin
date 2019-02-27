package com.wolf.test.entity;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/02/27
 */
public class RoomImpl implements Room {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.wolf.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/02/27
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomImpl2 {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

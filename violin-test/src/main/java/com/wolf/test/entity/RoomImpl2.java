package com.wolf.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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

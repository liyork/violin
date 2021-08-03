package com.wolf.test.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.text.NumberFormat;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/02/27
 */
@JsonSerialize(using = NumberSerializer.class)
public class RoomImpl3 {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}

// 注意JsonSerializer保持一致，都用fast
class NumberSerializer extends JsonSerializer<RoomImpl3> {
    @Override
    public void serialize(RoomImpl3 value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(value.getId() + "xxx");
    }
}

package com.wolf.test.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolf.test.entity.Room;
import com.wolf.test.entity.RoomImpl;

import java.util.Map;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/02/27
 */
public class JacksonTest {

    public static void main(String[] args) {

        testInterface();
    }

    private static void testInterface() {
        Room room = new RoomImpl();
        room.setId(1);
        room.setName("xxxxx");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(room, Map.class);

//        Room room1 = mapper.convertValue(map, Room.class);//报错
        Room room1 = mapper.convertValue(map, RoomImpl.class);
        System.out.println(room+" "+room1);
        System.out.println(room1.getId()+" "+room.getName());
    }
}

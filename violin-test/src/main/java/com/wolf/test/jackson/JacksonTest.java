package com.wolf.test.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wolf.test.entity.Room;
import com.wolf.test.entity.RoomImpl;
import com.wolf.test.entity.RoomImpl2;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/02/27
 */
public class JacksonTest {

    public static void main(String[] args) {

//        testInterface();
        testHasNotProperty();
    }

    private static void testInterface() {
        Room room = new RoomImpl();
        room.setId(1);
        room.setName("xxxxx");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(room, Map.class);

        Room room1 = mapper.convertValue(map, Room.class);//报错
//        Room room1 = mapper.convertValue(map, RoomImpl.class);
        System.out.println(room + " " + room1);
        System.out.println(room1.getId() + " " + room.getName());
    }

    private static void testHasNotProperty() {
        Room room = new RoomImpl();
        room.setId(1);
        room.setName("xxxxx");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(room, Map.class);

        //少个字段,Unrecognized field "name"，需要添加@JsonIgnoreProperties(ignoreUnknown = true)
        RoomImpl2 room1 = mapper.convertValue(map, RoomImpl2.class);
    }


    @Test
    public void testXml2Bean() throws Exception {
        String s = "/Users/chaoli/intellijWrkSpace/violin/violin-test/src/main/java/com/wolf/test/jackson/a.xml";

        ObjectMapper mapper = new XmlMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

        ItemList itemList = mapper.readValue(new File(s), ItemList.class);
        System.out.println(itemList);
    }
}

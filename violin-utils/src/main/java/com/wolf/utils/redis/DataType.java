
package com.wolf.utils.redis;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 上午11:48:15

 * @since 3.3
 */
public enum DataType {

    NONE("none"), VALUE("string"), LIST("list"), SET("set"), ZSET("zset"), HASH("hash");

    private static final Map<String, DataType> codeLookup = new ConcurrentHashMap<String, DataType>(6);

    static {
        for (DataType type : EnumSet.allOf(DataType.class))
            codeLookup.put(type.code, type);

    }

    private final String code;

    DataType(String name) {
        this.code = name;
    }

    public String code() {
        return code;
    }

    public static DataType fromCode(String code) {
        DataType data = codeLookup.get(code);
        if (data == null)
            throw new IllegalArgumentException("unknown data type code");
        return data;
    }
}

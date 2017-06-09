package com.wolf.utils.redis.cfcenter;

import java.io.Serializable;

/**

 * 客户端数据源
 */
public class ClientDataSource implements Serializable {

    private static final long serialVersionUID = 7554932720970300437l;
    /**
     * 值类型
     * 0为普通数据源
     * 1为公共数据源
     */
    private byte sourceType = 0;

    /**
     * 数据源名称
     */
    private String sourceName;
    /**
     * 数据源的值
     */
    private String sourceValue;

    /**
     * 组名称
     */
    private String groupName = "string";


    public byte getSourceType() {
        return sourceType;
    }

    public void setSourceType(byte sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    /**
     * @return
     */
    public String getSourceValue() {

        return this.sourceValue;

    }

    public void setSourceValue(String sourceValue)  {

        this.sourceValue = sourceValue;

    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}

package com.wolf.utils.redis;


import org.apache.commons.lang3.StringUtils;

/**

 */
public class RedisServerConfig implements Comparable<RedisServerConfig>{
    /**
     * redis实例名称（兼容客户端）
     */
    private String name;
    /**
     * redis实例rw名称（兼容客户端）
     */
    private String rwName;
    /**
     * 一个或者多个host实例，放在前面的节点为主节点，放在后面为从节点
     * IP和PORT之间通过:分隔开，不同server之间同构,分隔开
     */
    private String hostInfo;

    /**
     * 集群状态
     * 0为正常情况
     * -1为集群节点全部失效
     */
    private int status = 0;

    /**
     * slot分组
     */
    private String slotGroup;

    /**
     * 权重，值应该为从0-99
     */
    private String weight = "0-99";

    /**
     * 起始
     */
    private int beginWeight = -1;

    /**
     * 终止
     */
    private int endWeight = -1;

    private static final String SPEAR = "-";

    public String getName() {
    	if(StringUtils.isBlank(name)) {
    		return rwName;
    	}
        return name;
    }

    public String getName(int indexNum) {
        if(StringUtils.isBlank(name)) {
            return rwName+indexNum;
        } else {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRwName() {
        return rwName;
    }

    public void setRwName(String rwName) {
        this.rwName = rwName;
    }

    public String getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(String hostInfo) {
        this.hostInfo = hostInfo;
    }

    @Override
    public int compareTo(RedisServerConfig o) {
        if(o != null && !StringUtils.isBlank(o.getRwName())) {
            return this.getRwName().compareTo(o.getRwName());
        } else {
            return 0;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSlotGroup() {
        return slotGroup;
    }

    public void setSlotGroup(String slotGroup) {
        this.slotGroup = slotGroup;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        if(weight != null && !weight.trim().equals("")) {
            this.weight = weight;
        }
        //设置正则
        if(StringUtils.isNotBlank(weight)) {
            String[] data = weight.split(SPEAR);
            if(data.length == 2) {
                String tempBeginWeight = data[0];
                String tempEndWeight = data[1];
                beginWeight = Integer.valueOf(tempBeginWeight);
                endWeight = Integer.valueOf(tempEndWeight);
            }
        }
    }

    /**
     * 验证是否属于该区间
     * @param weight
     * @return
     */
    public boolean vertifyIsInWeight(String weight) {
        if(StringUtils.isBlank(weight) || beginWeight == -1 || endWeight == -1 ) {
            return false;
        }
        int weightInt = 0;
        if(weight.length() == 2 && weight.substring(0,1).equals("0")) {
            weightInt = Integer.valueOf(weight.substring(1,2));
        } else {
            weightInt = Integer.valueOf(weight);
        }

        if(weightInt >= beginWeight && weightInt < endWeight) {
            return true;
        } else {
            return false;
        }
    }

}
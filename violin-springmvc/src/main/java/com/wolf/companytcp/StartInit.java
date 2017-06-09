package com.wolf.companytcp;


/**
 * 扩展 IStart 可以设置参数
 */
public abstract class StartInit implements IStartInit {

    private String param;

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}

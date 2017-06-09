/**
 * Description: RemoteServiceVO.java
 * All Rights Reserved.
 *

 */
package com.wolf.companytcp;

import java.io.Serializable;

/**
 * 装载远程服务信息
 */
public class ServiceInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5657251975962463980L;

    private String name;

    private String beanId;

    private String descipt;

    private long connectionTimeOut;

    private long readTimeOut;

    private int needResult;

    private String methodName;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public int getNeedResult() {
        return needResult;
    }

    public void setNeedResult(int needResult) {
        this.needResult = needResult;
    }


    public long getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(long connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public long getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public String getDescipt() {
        return descipt;
    }

    public void setDescipt(String descipt) {
        this.descipt = descipt;
    }

}

package com.wolf.companytcp;

import java.io.Serializable;

/**
 * hessian模型类(封装了客户端的方法参数定义)
 */
public class TransferModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 唯一标识 */
    private long id;

    private Object object;

    private String projectName;

    private Integer grayValue;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Integer getGrayValue() {
        return grayValue;
    }

    public void setGrayValue(Integer grayValue) {
        this.grayValue = grayValue;
    }
}

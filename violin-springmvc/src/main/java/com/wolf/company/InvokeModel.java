package com.wolf.company;


import java.io.Serializable;

public class InvokeModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String className;
    private String methodName;
    private Class[] paramTypes;
    private Object[] params;
    private Object result;
    private String serviceId;
    private String sessionId;
    private RemoteResult resultVO;

    public InvokeModel() {

    }

    public InvokeModel(String className, String methodName, Class[] paramTypes, Object[] params, String serviceId, String sessionId) {
        this.className = className;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.params = params;
        this.serviceId = serviceId;
        this.sessionId = sessionId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public RemoteResult getResultVO() {
        return resultVO;
    }

    public void setResultVO(RemoteResult resultVO) {
        this.resultVO = resultVO;
    }

    public String toString() {
        return "className=" + className + " methodName=" + methodName;
    }

}

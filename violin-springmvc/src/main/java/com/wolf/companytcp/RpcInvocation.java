/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wolf.companytcp;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * RPC统一调用对象
 */
public class RpcInvocation implements Serializable {

    private static final long serialVersionUID = -4355285085441097045L;

    private String methodName;

    private String serviceid;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private HttpSession session;

    private Map<String, String> attachments;


    public RpcInvocation() {
    }


    public RpcInvocation(String serviceid, HttpSession session, Object[] arguments) {
        super();
        this.serviceid = serviceid;
        this.arguments = arguments;
        this.session = session;
    }


    public String getServiceid() {
        return serviceid;
    }


    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }


    public HttpSession getSession() {
        return session;
    }


    public void setSession(HttpSession session) {
        this.session = session;
    }


    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes == null ? new Class<?>[0] : parameterTypes;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments == null ? new Object[0] : arguments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments == null ? new HashMap<String, String>() : attachments;
    }

    public void setAttachment(String key, String value) {
        if(attachments == null) {
            attachments = new HashMap<String, String>();
        }
        attachments.put(key, value);
    }

    public void setAttachmentIfAbsent(String key, String value) {
        if(attachments == null) {
            attachments = new HashMap<String, String>();
        }
        if(!attachments.containsKey(key)) {
            attachments.put(key, value);
        }
    }

    public void addAttachments(Map<String, String> attachments) {
        if(attachments == null) {
            return;
        }
        if(this.attachments == null) {
            this.attachments = new HashMap<String, String>();
        }
        this.attachments.putAll(attachments);
    }

    public void addAttachmentsIfAbsent(Map<String, String> attachments) {
        if(attachments == null) {
            return;
        }
        for(Map.Entry<String, String> entry : attachments.entrySet()) {
            setAttachmentIfAbsent(entry.getKey(), entry.getValue());
        }
    }

    public String getAttachment(String key) {
        if(attachments == null) {
            return null;
        }
        return attachments.get(key);
    }

    public String getAttachment(String key, String defaultValue) {
        if(attachments == null) {
            return defaultValue;
        }
        String value = attachments.get(key);
        if(value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }


    public String toString() {
        return "RpcInvocation [methodName=" + methodName + ", parameterTypes=" + Arrays.toString(parameterTypes) + ", arguments=" + Arrays.toString(arguments) + ", attachments=" + attachments + "]";
    }

}
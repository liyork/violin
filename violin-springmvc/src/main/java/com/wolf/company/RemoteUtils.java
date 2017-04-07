/**
 *
 */
package com.wolf.company;


public final class RemoteUtils {

    private RemoteUtils() {

    }

    public static String getProjectNameToServiceId(String serviceId) {

        int e = serviceId.indexOf(".");
        if(e > -1) {
            return serviceId.substring(0, e).trim();
        } else {
            throw new RuntimeException("服务名称配置错误，请检查 !");
        }

    }

}

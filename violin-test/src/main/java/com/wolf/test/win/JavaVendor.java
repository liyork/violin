package com.wolf.test.win;

/**
 * Description:
 * Created on 2021/9/3 5:08 PM
 *
 * @author 李超
 * @version 0.0.1
 */
class JavaVendor {
    private static final boolean IBM_JAVA = System.getProperty("java.vendor").toUpperCase().contains("IBM");

    JavaVendor() {
    }

    public static boolean isIBM() {
        return IBM_JAVA;
    }

    public static String getKrb5LoginModuleName() {
        return isIBM() ? "com.ibm.security.auth.module.Krb5LoginModule" : "com.sun.security.auth.module.Krb5LoginModule";
    }

    public static int getSpnegoLifetime() {
        return isIBM() ? 2147483647 : 0;
    }
}


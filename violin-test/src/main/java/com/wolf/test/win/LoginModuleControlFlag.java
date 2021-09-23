package com.wolf.test.win;//package com.wolf.test.win;
//
///**
// * Description:
// * Created on 2021/9/3 5:09 PM
// *
// * @author 李超
// * @version 0.0.1
// */
//class LoginModuleControlFlag {
//
//    private String controlFlag;
//
//    /**
//     * Required {@code LoginModule}.
//     */
//    public static final LoginModuleControlFlag REQUIRED =
//            new LoginModuleControlFlag("required");
//
//    /**
//     * Requisite {@code LoginModule}.
//     */
//    public static final LoginModuleControlFlag REQUISITE =
//            new LoginModuleControlFlag("requisite");
//
//    /**
//     * Sufficient {@code LoginModule}.
//     */
//    public static final LoginModuleControlFlag SUFFICIENT =
//            new LoginModuleControlFlag("sufficient");
//
//    /**
//     * Optional {@code LoginModule}.
//     */
//    public static final LoginModuleControlFlag OPTIONAL =
//            new LoginModuleControlFlag("optional");
//
//    private LoginModuleControlFlag(String controlFlag) {
//        this.controlFlag = controlFlag;
//    }
//
//    /**
//     * Return a String representation of this controlFlag.
//     *
//     * <p> The String has the format, "LoginModuleControlFlag: <i>flag</i>",
//     * where <i>flag</i> is either <i>required</i>, <i>requisite</i>,
//     * <i>sufficient</i>, or <i>optional</i>.
//     *
//     * @return a String representation of this controlFlag.
//     */
//    public String toString() {
//        return (sun.security.util.ResourcesMgr.getString
//                ("LoginModuleControlFlag.") + controlFlag);
//    }
//}
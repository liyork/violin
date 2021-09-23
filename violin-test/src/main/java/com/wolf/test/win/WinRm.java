package com.wolf.test.win;

/**
 * Description:
 * Created on 2021/9/3 5:20 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class WinRm {

    private String userName;
    private String password;
    private Boolean useSsl;
    private String ip;
    private int port;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getUseSsl() {
        return useSsl;
    }

    public void setUseSsl(Boolean useSsl) {
        this.useSsl = useSsl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

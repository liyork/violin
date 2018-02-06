package com.wolf.test.jibx;

/**
 * Description:
 * <br/> Created on 2018/2/6 9:57
 *
 * @author 李超
 * @since 1.0.0
 */
public class AccountArray {

    private Account[] accounts;
    private int size;
    public int getSize() {
        size = accounts.length;
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public Account[] getAccounts() {
        return accounts;
    }
    public void setAccounts(Account[] accounts) {
        this.accounts = accounts;
    }
}

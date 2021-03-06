package com.wolf.test.jibx;

/**
 * Description:
 * <br/> Created on 2018/2/6 9:40
 *
 * @author 李超
 * @since 1.0.0
 */
public class Account {

    private int id;
    private String name;
    private String email;
    private String address;
    private Birthday birthday;
    //getter、setter

    @Override
    public String toString() {
        return this.id + "#" + this.name + "#" + this.email + "#" + this.address + "#" + this.birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Birthday getBirthday() {
        return birthday;
    }

    public void setBirthday(Birthday birthday) {
        this.birthday = birthday;
    }
}

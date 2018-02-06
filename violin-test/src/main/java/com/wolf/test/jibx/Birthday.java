package com.wolf.test.jibx;

/**
 * Description:
 * <br/> Created on 2018/2/6 9:40
 *
 * @author 李超
 * @since 1.0.0
 */
public class Birthday {

    private String birthday;

    public Birthday(String birthday) {
        super();
        this.birthday = birthday;
    }
    //getter、setter
    public Birthday() {}

    @Override
    public String toString() {
        return this.birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}

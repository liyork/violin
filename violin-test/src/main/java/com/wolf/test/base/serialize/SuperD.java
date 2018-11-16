package com.wolf.test.base.serialize;

/**
 * Description:父类未继承serializable
 * Created on 16/11/2018 6:18 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class SuperD {
    int supervalue;

    public SuperD(int supervalue) {
        this.supervalue = supervalue;
    }

    public SuperD() {
    }//增加一个无参的constructor

    public String toString() {
        return "supervalue: " + supervalue;
    }
}

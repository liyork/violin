package com.wolf.test.base.serialize;

import java.io.Serializable;

/**
 * Description:父类继承serializable
 * Created on 16/11/2018 6:15 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class SuperC implements Serializable {
    int supervalue;

    public SuperC(int supervalue) {
        this.supervalue = supervalue;
    }

    public String toString() {
        return "supervalue: " + supervalue;
    }
}

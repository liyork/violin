package com.wolf.test.base.serialize;

/**
 * Description:子类未继承serializable
 * Created on 16/11/2018 6:15 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class SubC extends SuperC {
    int subvalue;

    public SubC(int supervalue, int subvalue) {
        super(supervalue);
        this.subvalue = subvalue;
    }

    public String toString() {
        return super.toString() + " sub: " + subvalue;
    }
}
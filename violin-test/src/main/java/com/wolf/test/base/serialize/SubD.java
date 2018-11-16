package com.wolf.test.base.serialize;

import java.io.IOException;
import java.io.Serializable;

/**
 * Description:子类继承serializable
 * Created on 16/11/2018 6:19 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class SubD extends SuperD implements Serializable {
    int subvalue;

    public SubD(int supervalue, int subvalue) {
        super(supervalue);
        this.subvalue = subvalue;
    }

    public String toString() {
        return super.toString() + " sub: " + subvalue;
    }

    //下面可以解开
    //private void writeObject(java.io.ObjectOutputStream out)
    //        throws IOException {
    //    out.defaultWriteObject();//先序列化对象
    //    out.writeInt(supervalue);//再序列化父类的域
    //}
    //
    //private void readObject(java.io.ObjectInputStream in)
    //        throws IOException, ClassNotFoundException {
    //    in.defaultReadObject();//先反序列化对象
    //    supervalue = in.readInt();//再反序列化父类的域
    //}
}
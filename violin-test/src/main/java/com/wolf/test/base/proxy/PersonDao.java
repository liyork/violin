package com.wolf.test.base.proxy;

/**
 * Description:
 * <br/> Created on 18/09/2018 12:25 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class PersonDao {

    public void save() {
        System.out.println("save");
    }

    public final void finalSave() {
        System.out.println("finalSave");
    }

    public static void staticSave() {
        System.out.println("staticSave");
    }
}

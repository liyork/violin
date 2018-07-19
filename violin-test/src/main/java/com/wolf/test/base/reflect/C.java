package com.wolf.test.base.reflect;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <br/> Created on 2016/10/27 14:49
 *
 * @author 李超()
 * @since 1.0.0
 */
public class C<E> extends B {

    public C() {
    }

    public C(Integer age) {
        this.age = age;
    }

    private C(String seq) {
        this.seq = seq;
    }

    List<Integer> list = new ArrayList<>();

    private Integer age;

    public String seq;

    public String[] arr;

    public final String finalTest="abc";

    public State state = State.DRIVING;

    public void testaaa() {

    }

    public Integer testbbb(String a ,Integer b) {

        return 222;
    }

    public Integer testbbb(Integer a ,Integer b) {

        return 333;
    }

    public static Integer testStatic() {

        return 444;
    }

    public <T> Integer testGeneric(T t) {

        return 333;
    }

    public Integer testGeneric2(E t) {

        return 555;
    }

    public void testddd(List<String> a ,Integer b) {

    }

    public void testException() {
        throw new RuntimeException("xxx");
    }

    private List<String> testccc() {

        return null;
    }

    public Integer getAge() {
        return age;
    }

    public enum State {
        IDLE,
        DRIVING,
        STOPPING,

        test();

        int test1() {
            return 0;
        }

    }
}

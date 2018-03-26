package com.wolf.test.jdknewfuture;

/**
 * Description:
 * <br/> Created on 2017/12/19 14:20
 *
 * @author 李超
 * @since 1.0.0
 */
class Person {
    String firstName;
    String lastName;

    Person() {
    }

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}

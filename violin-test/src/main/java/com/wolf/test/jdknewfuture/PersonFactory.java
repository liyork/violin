package com.wolf.test.jdknewfuture;

/**
 * Description:
 * <br/> Created on 24/08/2018 9:35 AM
 *
 * @author 李超
 * @since 1.0.0
 */
interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}
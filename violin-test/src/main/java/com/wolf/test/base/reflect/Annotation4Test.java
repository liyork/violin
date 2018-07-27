package com.wolf.test.base.reflect;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Annotation4Test {

    String domain() ;

}

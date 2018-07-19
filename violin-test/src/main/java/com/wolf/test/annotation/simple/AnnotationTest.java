package com.wolf.test.annotation.simple;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Description:
 * <br/> Created on 10/07/2018 7:36 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class AnnotationTest {

    public static void main(String[] args) {

        System.out.println(MyAnnotation.class.isAnnotation());

        Class<UserAnnotation> userAnnotationClass = UserAnnotation.class;
        boolean annotationPresent = userAnnotationClass.isAnnotationPresent(MyAnnotation.class);
        System.out.println(annotationPresent);

        if (annotationPresent) {
            MyAnnotation annotation = userAnnotationClass.getAnnotation(MyAnnotation.class);
            System.out.println(annotation.annotationType());
            System.out.println(annotation.id() + "_" + annotation.msg());
        }

        try {
            Method test = userAnnotationClass.getMethod("test");
            boolean annotationPresent1 = test.isAnnotationPresent(MyAnnotation.class);
            if (annotationPresent1) {
                MyAnnotation annotation = test.getAnnotation(MyAnnotation.class);
                System.out.println(annotation.id() + "_" + annotation.msg());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void actAnnotation() {
        AnnotationAct annotationAct = new AnnotationAct();
        Class annotationActClass = annotationAct.getClass();

        Method[] methods = annotationActClass.getMethods();
        for (Method method : methods) {
            boolean annotationPresent = method.isAnnotationPresent(MyAnnotation.class);
            if (annotationPresent) {
                try {
                    method.invoke(annotationAct, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

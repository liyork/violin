package com.wolf.test.base.reflect;

import com.wolf.test.annotation.BusinessService;
import com.wolf.test.annotation.CacheResult;
import com.wolf.utils.ReflectUtils;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Description:没有反射就没有框架
 * Person person = new Person()
 * Class clazz = person.getClass()
 * person是类Person的实例对象。类Person是java.lang.Class的实例对象,Class是Class Type。多个person对应的Class是一个
 * Class是Person类的类类型
 * <br/> Created on 2016/10/27 15:08
 *
 * @author 李超()
 * @since 1.0.0
 */
public class ReflectTest {

    public static void main(String[] args) throws Exception {
//        testBase();
        testDynamicCreateBean();
//        test();
    }

    private static void testBase() {
        Class<C> cClass = C.class;
        String name = cClass.getName();
        System.out.println("cClass name:" + name);//com.wolf.test.base.reflect.C
        String simpleName = cClass.getSimpleName();
        System.out.println("cClass simpleName:" + simpleName);

        Method[] methods = cClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("methods name:" + method.getName());
            Class<?> returnType = method.getReturnType();
            System.out.println("returnType:" + returnType.getName());
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                System.out.print("parameter:" + parameterType.getName() + ",");
            }
            System.out.println();
        }

        System.out.println();

        Field[] fields = cClass.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("field name:" + field.getName());
            Class<?> type = field.getType();
            System.out.println("field type:" + type);
            System.out.println();
        }

        System.out.println();
        Constructor<?>[] constructors = cClass.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println("constructor name:" + constructor.getName());
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                System.out.print("parameter:" + parameterType.getName() + ",");
            }
        }
    }

    //我们想要的就是我用那个类就加载那个类，也就是常说的运行时刻加载，动态加载类
    private static void testDynamicCreateBean() throws Exception {
        Class<?> aClass = Class.forName("com.wolf.test.base.reflect.C");
        Object o = aClass.newInstance();
        System.out.println(o);
    }

    private static void test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        //仅能取到公共方法
        Method test1 = C.class.getMethod("test", null);
        System.out.println(test1);
        Method test2 = B.class.getMethod("test", null);
        System.out.println(test2);
        System.out.println(test1.equals(test2));
        Method test3 = A.class.getMethod("test", null);
        System.out.println(test3);
        System.out.println(test1.equals(test3));

        //所有声明的方法
        Method test4 = B.class.getDeclaredMethod("test1", null);
        System.out.println(test4);
        //不让jvm检查方法修饰符，我们可以直接访问到任何修饰符的方法
        test4.setAccessible(true);
        Object invoke = test4.invoke(new B(), null);
        System.out.println(invoke);
        System.out.println("test4.isAccessible():" + test4.isAccessible());
        test4.setAccessible(false);

        //似乎是不用恢复成false，因为每次get都不一样的method。
        Method test5 = B.class.getDeclaredMethod("test1", null);
        System.out.println("test4==test5:" + (test4 == test5));
        System.out.println("test5.isAccessible():" + test5.isAccessible());

        B b = new B();
        Field name = B.class.getDeclaredField("name");
        name.setAccessible(true);
        name.set(b, "XXXX");
        name.setAccessible(false);
        System.out.println(b.getName());
    }

    @Test
    public void testAnnotationInstance() throws NoSuchMethodException {
        Method getCityName = BusinessService.class.getMethod("getCityName", null);
        Annotation[] annotations = getCityName.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(CacheResult.class.isInstance(annotation));
            System.out.println(annotation instanceof CacheResult);
        }
    }

    //看来类结构中存储自己，父类只有个外键关联
    @Test
    public void testGetField() throws NoSuchMethodException {
        Field name = ReflectUtils.getField(C.class, "name");
        System.out.println(name);

        Collection<Field> allFields = ReflectUtils.getAllFields(C.class);
        for (Field allField : allFields) {
            System.out.println(allField);
        }
    }
}

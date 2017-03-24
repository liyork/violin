package com.wolf.test.generics;

import com.wolf.test.entity.Person;
import com.wolf.test.generics.entity.Children;
import com.wolf.test.generics.entity.SubChildren;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p> Description:泛型测试
 * <p/>
 * Date: 2015/7/21
 * Time: 8:44
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class GenericTest {

    @Test
    public void testBasicUse() throws NoSuchMethodException {
        CustomizeClass<Person> customizeClass = new CustomizeClass<Person>();
        ParameterizedType genericInterface = (ParameterizedType) customizeClass.getClass().getGenericInterfaces()[0];
        Type[] actualTypeArguments = genericInterface.getActualTypeArguments();
        System.out.println(actualTypeArguments[0]);
        System.out.println(genericInterface.getOwnerType());
        System.out.println(genericInterface.getRawType());
        ParameterizedType superclass = (ParameterizedType) customizeClass.getClass().getGenericSuperclass();
        Type[] actualTypeArguments1 = superclass.getActualTypeArguments();
        System.out.println(actualTypeArguments1[0]);
        System.out.println(superclass.getOwnerType());
        System.out.println(superclass.getRawType());

        Method setEntity = customizeClass.getClass().getMethod("setEntity", Object.class);
        TypeVariableImpl type = (TypeVariableImpl) setEntity.getGenericParameterTypes()[0];
        System.out.println(type.getName());
        System.out.println(Arrays.toString(type.getBounds()));
        System.out.println(type.getGenericDeclaration());

        Method setEntity1 = customizeClass.getClass().getMethod("setEntity1", Person.class);
        Class type1 = (Class) setEntity1.getGenericParameterTypes()[0];
        System.out.println(type1.getName());
    }

    @Test
    public void testBasicGenericClass() {
        BasicGenericClass<Person> basicGenericClass = new BasicGenericClass<Person>();
        Person person1 = new Person(111, "xx");
        basicGenericClass.setT(person1);
        Person x = basicGenericClass.getX();
        System.out.println(x.getId());
        BasicGenericClass<Children> basicGenericClass1 = new BasicGenericClass<Children>();
        //成功编译过后的class文件中是不包含任何泛型信息的。泛型信息不会进入到运行时阶段。
        System.out.println(basicGenericClass.getClass() == basicGenericClass1.getClass());
    }

    @Test
    public void testUpperAndSuperLimit() {
//		UpperAndSuperLimit upperLimit1 = new UpperAndSuperLimit<Person>();//报错
        UpperAndSuperLimit upperLimit2 = new UpperAndSuperLimit<Children>();
        UpperAndSuperLimit upperLimit3 = new UpperAndSuperLimit<SubChildren>();

//		UpperLimit.down(new ArrayList<SubChildren>());//报错
        ArrayList<Children> list = new ArrayList<>();
        list.add(new Children(1, "x1"));
        list.add(new Children(2, "x2"));
        UpperAndSuperLimit.down(list);
        UpperAndSuperLimit.down(new ArrayList<Person>());

//		UpperLimit.up(new ArrayList<Person>());//报错
        UpperAndSuperLimit.up(list);
        UpperAndSuperLimit.up(new ArrayList<SubChildren>());


        Children children = UpperAndSuperLimit.testGenericMethod1(list);
        System.out.println(children);
        Object o = new UpperAndSuperLimit().testGenericMethod2(list);
        System.out.println(o);

        Object o1 = UpperAndSuperLimit.ifThenElse(false, 1, 2);
        System.out.println(o1);
        Object o2 = UpperAndSuperLimit.ifThenElse(false, "1", 3.14d);
        System.out.println(o2);
        Object o3 = UpperAndSuperLimit.ifThenElse(false, new SubChildren(1, "2"), 3.14d);
        System.out.println(o3);

        CustomizeClass<Person> customizeClass = new CustomizeClass<Person>();
        Person person = new Person(1, "xx");
        customizeClass.setEntity(person);
        Person entity = customizeClass.getEntity();
        CustomizeClass<Children> customizeClass2 = new CustomizeClass<Children>();
        customizeClass2.setEntity(new Children(2, "yy"));
        boolean compare = UpperAndSuperLimit.compare(customizeClass, customizeClass2);
        System.out.println(compare);
    }

    //测试使用泛型导致的参数被两次Object[]
    @Test
    public void testDynamicParamMethod() {
        Object[] arg = new Object[2];
        arg[0] = 1l;
        arg[1] = 0;
        GenericTest.dynamicParamMethod("a1", "a2", arg);
        GenericTest.dynamicParamMethod("a1", "a2", "a3", "a4");

        GenericXImpl generic = new GenericXImpl();
        Object[] key = generic.getKey();
        System.out.println(key);
        GenericTest.dynamicParamMethod("a1", "a4", key);

        generic.test();
        generic.test2();
    }

    private static void dynamicParamMethod(String a, String b, Object... arg) {
        System.out.println("a:" + a + " " + arg[0]);
    }

    abstract class GenericX<K> {
        abstract K getKey();

        public void test() {
            K key = getKey();
            GenericTest.dynamicParamMethod("a1", "a2", key);

            //编译后的结果。。如果方法是可变参数，则new Object[] { }
//            Object key = getKey();
//            JavaBaseTest.testDynamicParamMethod("a1", "a2", new Object[] { key });
        }

        public void test2() {
            K key = getKey();
            GenericTest.dynamicParamMethod("a1", "a2", (Object[]) key);

            //编译后的结果。。如果方法是可变参数，且有强制转换，则不使用new Object[] {}
//            Object key = getKey();
//            GenericTest.dynamicParamMethod("a1", "a2", (Object[])key);
        }
    }

    class GenericXImpl extends GenericX<Object[]> {

        @Override
        Object[] getKey() {
            Object[] arg = new Object[2];
            arg[0] = 1l;
            arg[1] = 0;
            return arg;
        }

    }
}


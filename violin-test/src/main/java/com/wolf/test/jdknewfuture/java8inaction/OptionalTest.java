package com.wolf.test.jdknewfuture.java8inaction;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Description: optional test
 *
 * @author 李超
 * @date 2019/07/31
 */
public class OptionalTest {

    //建模，参考optional
    class Person {
        private Optional<Car> car;//对没有的属性建模

        public Optional<Car> getCar() {
            return car;
        }

        public void setCar(Optional<Car> car) {
            this.car = car;
        }

        //Optional的设计初衷仅仅是要支持能返回Optional对象的语法,由于Optional类设计时就没特别考虑将其作为类的字段使用，
        // 所以它也并未实现Serializable接口,所以若是应用需要序列化，那么只能提供一个返回Optional的方法
        public Optional<Car> getCarAsOptional() {
            Car car1 = null;
            return Optional.ofNullable(car1);
        }
    }

    class Car {
        private Optional<Insurance> insurance;

        public Optional<Insurance> getInsurance() {
            return insurance;
        }

        public void setInsurance(Optional<Insurance> insurance) {
            this.insurance = insurance;
        }
    }

    class Insurance {
        private String name;//必须存在

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void testCreate() {
        Optional<Car> empty = Optional.empty();

        Car car = new Car();
        Optional<Car> car1 = Optional.of(car);//若car=null立即抛出异常

        Optional<Car> car2 = Optional.ofNullable(car);
    }

    @Test
    public void testProcessNullFilterChain() {

        //问题代码-并未做空检验
//        person.getCar().getInsurance().getName();

        Insurance insurance = new Insurance();
        Optional<Insurance> insurance1 = Optional.of(insurance);
        Optional<String> name = insurance1.map(Insurance::getName);

        Person person = new Person();
        //错误方式
//        Optional<String> name = Optional.of(person)
//                .map(Person::getCar)==>Optional<Optional<Car>>
//                .map(Car::getInsurance)
//                .map(Insurance::getName);

        //处理潜在null的方式
        Optional<String> s = Optional.of(person)
                .flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName);
    }

    @Test
    public void testGetValue() {

        String s = "abc";
        Optional<String> ins = Optional.ofNullable(s);
        String nullValue = ins.orElse("null value");
        String s1 = ins.orElseGet(() -> "abc");//延迟获取，为空时才调用supplier
    }

    @Test
    public void testProcessNull() {

        getInsuranceJudgeParam(null, null);

        Person person = new Person();
        getInsurance(Optional.of(person), Optional.empty());
    }

    //以前的解决不能为空的方式
    public Insurance getInsuranceJudgeParam(Person person, Car car) {

        if (null == person || null == car) {
            return null;
        }

        return getInsurance(person, car);
    }

    //允许为null的方法
    public Optional<Insurance> getInsurance(Optional<Person> person, Optional<Car> car) {
        //person/car不为空则执行嵌套lambda
        return person.flatMap(p -> car.map(c -> getInsurance(p, c)));
    }

    //不允许为null的方法
    public Insurance getInsurance(Person person, Car car) {
        Insurance insurance = new Insurance();
        return insurance;
    }

    @Test
    public void testFilter() {

        Insurance insurance = new Insurance();
        insurance.setName("xx");
        filterPre(insurance);
        filterNow(insurance);
    }

    //以前处理判断方式
    public void filterPre(Insurance insurance) {

        if (null != insurance && null != insurance.getName() && insurance.getName().equals("xx")) {
            System.out.println("xx is insurance");
        }
    }

    //可以将Optional看成最多包含一个元素的Stream对象
    public void filterNow(Insurance insurance) {

        Optional.ofNullable(insurance)
                .filter(i -> i.getName().equals("xx"))
                .ifPresent(i -> System.out.println("xx is insurance"));
    }

    @Test
    public void testAction() {
        //用optional对可能为null的进行包装
        Map<String, Object> map = new HashMap<>();
        Object xx = map.get("xx");
        if (null != xx) {
            System.out.println("xx is not null");
        }

        Optional<Object> xx1 = Optional.ofNullable(map.get("xx"));
        xx1.ifPresent(x -> System.out.println("xx is not null"));

        //封装异常处理，可放到工具类中
        string2Int("abc");
    }

    public Optional<Integer> string2Int(String s) {

        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Test
    public void testDiff() {

        Properties properties = new Properties();
        properties.put("a", 1);
        properties.put("b", true);
        properties.put("c", -1);
        properties.put("d", 0);
        properties.put("e", "xxx");

        assertEquals(1, useCommand("a", properties));
        assertEquals(0, useCommand("b", properties));
        assertEquals(0, useCommand("c", properties));
        assertEquals(0, useCommand("d", properties));
        assertEquals(0, useCommand("e", properties));

        assertEquals(1, useOptional("a", properties));
        assertEquals(0, useOptional("b", properties));
        assertEquals(0, useOptional("c", properties));
        assertEquals(0, useOptional("d", properties));
        assertEquals(0, useOptional("e", properties));
    }

    //只有val大于0则展示，其他展示0
    public int useCommand(String key, Properties properties) {

        Object o = properties.get(key);

        if (null != o) {
            try {
                int i = Integer.parseInt(o.toString());
                if (i > 0) {
                    return i;
                }
            } catch (NumberFormatException e) {

            }
        }
        return 0;
    }

    public int useOptional(String key, Properties properties) {

        return Optional.ofNullable(properties.get(key))
                .flatMap(i -> string2Int(i.toString()))
                .filter(i -> i > 0)
                .orElse(0);
    }

}

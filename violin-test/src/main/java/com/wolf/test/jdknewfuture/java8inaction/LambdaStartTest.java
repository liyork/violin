package com.wolf.test.jdknewfuture.java8inaction;

import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/07/18
 */
public class LambdaStartTest {

    public static void main(String[] args) {
        testBase();
    }

    private static void testBase() {
        //之前jdk需要把方法file.isHidden包裹在FileFilter对象中传入方法listFiles
        File[] files = new File(".").listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isHidden();
            }
        });

        //方法引用(::语法)-把这个方法作为值，方法作为一等公民，与值同等，可以传递。
        FileFilter isHidden = File::isHidden;
        new File(".").listFiles(isHidden);

        //函数，不在需要依附类上
        IntFunction<Integer> function = (int x) -> x + 1;
    }

    //老方式，通过构造匿名类对象实例传入行为
    @Test
    public void testRemoveDuplicateByObject() {

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        getLargeThanOne(list);
        getLargeThanTwo(list);

        getListByPredicate(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 1;
            }
        });

        getListByPredicate(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 2;
            }
        });
    }

    //行为参数化，行为作为参数传给方法，getListByPredicate的方法的行为用Predicate参数化
    @Test
    public void testRemoveDuplicateByMethodRef() {

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        getLargeThanOne(list);
        getLargeThanTwo(list);

        //解决方案：传递函数-不同部分
        getListByPredicate(list, LambdaStartTest::isLargeThanOne);
        getListByPredicate(list, LambdaStartTest::isLargeThanTwo);
    }

    //与getLargeThanTwo很多重复!!!
    private static List<Integer> getLargeThanOne(List<Integer> list) {
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer integer : list) {
            if (isLargeThanOne(integer)) {
                result.add(integer);
            }
        }
        return result;
    }

    private static List<Integer> getLargeThanTwo(List<Integer> list) {
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer integer : list) {
            if (isLargeThanTwo(integer)) {
                result.add(integer);
            }
        }
        return result;
    }

    private static boolean isLargeThanOne(Integer integer) {
        return integer > 1;
    }

    private static boolean isLargeThanTwo(Integer integer) {
        return integer > 2;
    }

    //感觉Predicate<Integer> predicate好像是个对象呢!但是调用方很像是方法
    private static List<Integer> getListByPredicate(List<Integer> list, Predicate<Integer> predicate) {
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer integer : list) {
            if (predicate.test(integer)) {
                result.add(integer);
            }
        }
        return result;
    }

    //lambda，匿名函数，对于仅用一次的方法，可以用匿名函数定义，省去了定义方法的麻烦。
    //要是Lambda的长度多于几行（它的行为也不是一目了然）的话，那你还是应该用方法引用来指向一个有描述性名称的方法，而不是使用匿名的Lambda。
    @Test
    public void testRemoveDuplicateByLambda() {

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        getLargeThanOne(list);
        getLargeThanTwo(list);

        //解决方案：传递函数-不同部分
        getListByPredicate(list, (integer) -> integer > 1);
        getListByPredicate(list, (integer) -> integer > 2);
    }

    //Java 8基于Stream的并行提倡很少使用synchronized的函数式编程风格，它关注数据分块而不是协调访问
    //Collection主要是为了存储和访问数据，而Stream则主要用于描述对数据的计算
    //其实Collections.sort方法真的应该属于List接口
    @Test
    public void testStreamSimple() {

        List<LbBean> list = new ArrayList<>();
        list.add(new LbBean(1, "green"));
        list.add(new LbBean(2, "green"));
        list.add(new LbBean(3, "red"));

        //原来方式筛选+分组，自己外部迭代
        Map<String, List<LbBean>> group = new HashMap<>();
        for (LbBean lbBean : list) {
            int money = lbBean.getMoney();
            if (money > 2) {
                String color = lbBean.getColor();
                List<LbBean> extLbBeans = group.get(color);
                if (CollectionUtils.isEmpty(extLbBeans)) {
                    extLbBeans = new ArrayList<>();
                    group.put(color, extLbBeans);
                }

                extLbBeans.add(lbBean);
            }
        }

        //新方案：数据处理完全是在库内部进行的,内部迭代
        list.stream().filter((lbBean -> lbBean.getMoney() > 2)).collect(Collectors.groupingBy(LbBean::getColor));

        ////函数式编程中的函数的主要意思是“把函数作为一等值”，不过它也常常隐含着第二层意思，即“执行时在元素之间无互动”，即独立。
        list.parallelStream().filter((lbBean -> lbBean.getMoney() > 2)).collect(Collectors.groupingBy(LbBean::getColor));
    }

    //Lambda表达式由参数、箭头和主体组成。
    // (parameters) -> expression or (parameters) -> { statements; }
    //抽象方法的签名（称为函数描述符）描述了Lambda表达式的签名
    @Test
    public void testLambdaSample() {

        List<String> list = new ArrayList<>();
        list.sort(String::compareTo);

        new Thread(() -> System.out.println("111")).start();

        Function<String, Integer> test1 = (String s) -> s.length();
        Function<LbBean, String> test2 = (LbBean lb) -> lb.getColor();
        BiConsumer<String, LbBean> test3 = (String s, LbBean lb) -> {
            System.out.println(s.length());
            System.out.println(lb.getColor());
        };
        Supplier<Integer> test4 = () -> 44;

        //(Integer i) -> return "Alan" + i; //return属于控制流语句，需要有{}，默认不写return会自动加上
    }

    @Test
    public void testBufferReaderProcessor() throws IOException {

        String s = processFile(new File("a"), (bufferedReader -> bufferedReader.readLine()));
        String s2 = processFile(new File("a"), (bufferedReader -> bufferedReader.readLine() + bufferedReader.readLine()));
    }

    //相同的模板方法：读取文件，操作，关闭文件，将操作行为参数化。
    private String processFile(File file, BufferedReaderProcessor bufferedReaderProcessor) throws IOException {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            return bufferedReaderProcessor.read(bufferedReader);
        }
    }

    @Test
    public void testConsumer() throws IOException {

        foreach(Arrays.asList(1, 2, 3, 4), (Integer a) -> System.out.println(a));
    }

    public void foreach(List<Integer> list, Consumer<Integer> consumer) {
        list.forEach(a -> consumer.accept(a));
    }

    //lambda表达式的签名要和FunctionalInterface一致
    @Test
    public void testFunction() throws IOException {

        List<String> list = map(Arrays.asList(1, 2, 3, 4), (Integer a) -> a + "");
    }

    public List<String> map(List<Integer> list, Function<Integer, String> function) {
        List<String> result = new ArrayList<>();
        list.forEach(a -> {
            String apply = function.apply(a);
            result.add(apply);
        });
        return result;
    }

    //避免自动装箱
    @Test
    public void testIntPredicate() throws IOException {

        IntPredicate intPredicate = (int a) -> a == 1;
    }

    //返回值为int避免自动装箱
    @Test
    public void testToIntFunction() throws IOException {

        ToIntFunction<Integer> toIntFunction = (Integer a) -> a + 1;
    }

    //lambda表达式推断，lambda表达式内参数类型推断
    //有时候显式写出类型更易读，有时候去掉它们更易读。没有什么法则说哪种更好；对于如何让代码更易读，程序员必须做出自己的选择。
    @Test
    public void testLambdaTypeDeduce() throws IOException {

        ToIntFunction<Integer> toIntFunction2 = a -> a + 1;//单参数可以省略括号。
        BiFunction<Integer, Integer, String> biFunction = (a, b) -> a + b + "";
    }

    int c = 2;

    //lambda使用自有变量（不是参数，而是在外层作用域中定义的变量）,称作捕获Lambda
    @Test
    public void testLambdaCatch() throws IOException {

        int a = 1;
        Runnable runable = () -> System.out.println(a);

        //外部变量必须显式声明为final，或事实上是final,Lambda表达式只能捕获指派给它们的外部变量一次。
        //外部局部变量保存在栈上，为了防止外部局部变量被回收，而lambda却使用的是副本而非原值，所以有此限制。
//        int b = 3;
//        Runnable runable2 = () -> System.out.println(b);
//        b = 3;

        int b = 3;
        Runnable runable2 = () -> {
            System.out.println(b);
//            b = 4;//不能在lambda中改变此方法的局部变量，否则会影响到原线程的逻辑进而引发并发问题。
        };

        //修改实例变量可以，实例变量本身代码就必须保证并发问题
        Runnable runable3 = () -> {
            System.out.println(c);
            c = 4;
        };
    }

    //方法引用让你可以重复使用现有的方法定义，并像Lambda一样传递它们
    //如果一个Lambda代表的只是“直接调用这个方法”，那最好还是用名称来调用它，而不是去描述如何调用它
    //方法引用就是替代那些转发参数的Lambda表达式的语法糖，方法内只用参数进行操作
    @Test
    public void testMethodRef() {

        //静态方法的方法引用
        Function<String, Integer> staticMethodRef1 = (String s) -> Integer.parseInt(s);
        Function<String, Integer> staticMethodRef2 = Integer::parseInt;
        //任意类型实例方法的方法引用
        Function<String, Object> anyObjectMethodRef1 = (String s) -> s.toUpperCase();
        Function<String, String> anyObjectMethodRef2 = String::toUpperCase;
        //现有对象的实例方法的方法引用
        LbBean lbBean = null;
        Supplier<Integer> appointObjectMethodRef = lbBean::getMoney;

        List<String> strings = Arrays.asList("1", "2", "3", "4");
        //Comparator中int compare(T o1, T o2)
        strings.sort((String a, String b) -> a.compareToIgnoreCase(b));
        strings.sort(String::compareToIgnoreCase);

        BiPredicate<List<String>, String> contains = (list1, e) -> list1.contains(e);
        BiPredicate<List<String>, String> contains2 = List::contains;


        List<LbBean> list = new ArrayList<>();
        //list.sort需要一个Comparator，Comparator.comparing返回一个Comparator，而参数是Function<T,U> keyExtractor，其描述符是R apply(T t)
        list.sort(Comparator.comparing((a) -> a.getMoney()));
        list.sort(Comparator.comparing(LbBean::getMoney));

    }

    //构造函数引用的语法是ClassName::new
    @Test
    public void testConstructorRef() {

        LbBean lbBean1 = new LbBean();
        Supplier<LbBean> lbBeanSupplier = LbBean::new;
        LbBean lbBean = lbBeanSupplier.get();

        LbBean lbBean2 = new LbBean(11);
        Function<Integer, LbBean> lbBeanFunction = LbBean::new;
        LbBean apply = lbBeanFunction.apply(11);

        LbBean lbBean3 = new LbBean(1, "a");
        BiFunction<Integer, String, LbBean> biFunction = LbBean::new;
        LbBean a = biFunction.apply(1, "a");

        giveMeFruit("apple", 11);
        giveMeFruit("orange", 22);
    }

    static Map<String, Function<Integer, Fruit>> map = new HashMap<>();

    static {
        map.put("apple", Apple::new);
        map.put("orange", Orange::new);
    }

    //不将构造函数实例化却能够引用它
    public static Fruit giveMeFruit(String fruit, Integer weight) {
        return map.get(fruit.toLowerCase()).apply(weight);
    }

    @Test
    public void testConcat() {

        Comparator<LbBean> c = Comparator.comparing(LbBean::getMoney);
        Comparator<LbBean> c2 = Comparator.comparing(LbBean::getMoney).reversed();

        List<LbBean> list = null;
        list.sort(c.thenComparing(c2));//c1比较后相同则用c2

        Predicate<LbBean> predicate = (LbBean lb) -> lb.getMoney() > 1;
        Predicate<LbBean> negate = predicate.negate();
        predicate.and((LbBean lb) -> lb.getColor().length() > 3)
                .or((LbBean lb) -> lb.getColor().length() < 5);

        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> function3 = f.andThen(g);//g(f(x))
        Integer apply = function3.apply(1);

        Function<Integer, Integer> function4 = f.compose(g);//f(g(x))

    }
}

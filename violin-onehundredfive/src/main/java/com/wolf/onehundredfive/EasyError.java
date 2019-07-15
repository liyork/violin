package com.wolf.onehundredfive;

import com.wolf.utils.ArrayUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p> Description: 151个简单错误
 * <p/>
 * Date: 2015/8/19
 * Time: 13:16
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class EasyError {

	private static String CONSTANT_VARIABLE = "XXX";

	public static void main(String[] args) throws Exception {
		new EasyError().test57();
	}

	//long 类型的l用大写
	public void test1() {
		long longString = 1L;
		System.out.println(longString);
	}

	//常量就是不能变的，不要用随机数
	public void test2() {
		System.out.println(CONSTANT_VARIABLE);
	}


	//三元操作符会提升变量类型
	public void test3() {
		String s = String.valueOf(80 < 90 ? 100 : 110.0);
		System.out.println(s);
	}


	//不要使用易混淆的重载方法
	public void test4() {
		class OverLoadClass {
			public void xx1(int a, int b) {

			}

			public void xx1(int a, int... b) {

			}
		}

//		new OverLoadClass().xx1(1);
	}


	//方法设计者有问题  kiss keep it simple stupid
	public void test5() {
		class OverLoadClass {
			public void xx1(int a, Integer... b) {
				System.out.println("111");
			}

			public void xx1(int a, String... b) {
				System.out.println("222");
			}
		}
//		new OverLoadClass().xx1(1);
//		调用者的错误
//		new OverLoadClass().xx1(1,null);
		//好的做法
		String s[] = null;
		new OverLoadClass().xx1(1, s);
	}


	//覆写要都一样
	public void test6() {
		class BaseClass {
			public void xx1(int a, Integer... b) {

			}
		}
		class SubClass extends BaseClass {

			@Override
			public void xx1(int a, Integer[] b) {

			}
		}

		BaseClass baseClass = new SubClass();
		baseClass.xx1(1, 1);

		SubClass subClass = new SubClass();
//		subClass.xx1(1,1);
	}


	//自增
	public void test7() {
		int count = 0;
		for (int i = 0; i < 10; i++) {
			count = count++;
		}

		System.out.println(count);
	}

	//少用break continue 或是跳出循环标号
	//少用静态导入
	//手动配置serialVersionUID
	//序列化对象不要使用构造函数为final赋值
	//当不想序列化时使用transient，也可以重写writeObject方法
	//case后一定要带break


	//instanceof
	public void test8() {
		//instanceof 左右有继承关系即可通过编译
		System.out.println(new Object() instanceof String);
//		System.out.println(new Date() instanceof String);

		//instanceof 只能用于对象
//		System.out.println('A' instanceof Character);

		//只要左边是null返回false
		System.out.println(null instanceof String);
		//null是万用类型，即无类型
		System.out.println((String) null instanceof String);

		//泛型编译后都是object类型
		new GenericClass<String>().test("");

	}

	class GenericClass<T> {
		public void test(T t) {
			System.out.println(t instanceof Date);
		}
	}


	//部署工程最好整体，不要单独文件，注意final修饰的基本类型被嵌入到字节码中而不是引用

	//取模  3 - 3/2*2   是否为偶数用==0判断，不要用1
	public void test9() {
		System.out.println(-1 % 2);
		System.out.println(3 % 2);
	}

	//浮点运算不准 使用bigDecimal
	public void test10() {
		System.out.println(10.00 - 9.6);//0.40000000000000036
		BigDecimal bigDecimal1 = new BigDecimal("10.00");
		BigDecimal bigDecimal2 = new BigDecimal("9.6");
		System.out.println(bigDecimal1.subtract(bigDecimal2));//0.40000000000000036
	}

	//java是先运算后转换,所以主动声明(long)，不要使用默认(int)
	public void test11() {
		int speed = 30 * 10000 * 1000;
		long i1 = speed * 60 * 8;
		System.out.println(i1);//-2028888064
		long i2 = 1L * speed * 60 * 8;
		System.out.println(i2);//144000000000
	}

	//边界检查很重要 0 最大 最小
	public void test12() {
		int maxValue = Integer.MAX_VALUE;
		int minValue = Integer.MIN_VALUE;
		System.out.println(maxValue);
		System.out.println(maxValue + 1);
		System.out.println(minValue);
	}

	//包装类型要做null判断
	public void test13() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(null);

		//自动拆箱调用intValue方法，null没有，所以报错NullPointerException
		for (int integer : list) {
			System.out.println(integer);
		}
	}

	//包装类型大小比较
	public void test14() {
		Integer integer1 = new Integer(100);
		Integer integer2 = new Integer(100);

		System.out.println(integer1 == integer2);//== 比较对象引用
		System.out.println(integer1.compareTo(integer2));
		//> < 只比较数值
		System.out.println(integer1 > integer2);
		System.out.println(integer1 < integer2);
	}

	//包装类型装箱使用valueOf，有缓存-128----127
	public void test15() {
		Integer integer1 = 100;
		Integer integer2 = 100;

		System.out.println(integer1 == integer2);

		Integer integer3 = 128;
		Integer integer4 = 128;

		System.out.println(integer3 == integer4);

	}


	//优先使用基本类型
	public void test16() {

		class BaseClass {
			public void xx1(long a) {
				System.out.println("基本类型");
			}

			public void xx1(Long a) {
				System.out.println("包装类型");
			}
		}

		new BaseClass().xx1(1);//编译器会自动加宽
		new BaseClass().xx1(Integer.valueOf(2));//先变成1再加宽
		new BaseClass().xx1(3l);
		new BaseClass().xx1(Long.valueOf(3l));//自动装箱

	}


	//种子一样，实例不一样，结果也一样
	public void test17() {
		Random random1 = new Random(1000);
		Random random2 = new Random(1000);
		Random random3 = new Random();

		for (int i = 0; i < 3; i++) {
			System.out.println("1===>" + random1.nextInt());
		}
		for (int i = 0; i < 3; i++) {
			System.out.println("2===>" + random2.nextInt());
		}
		for (int i = 0; i < 3; i++) {
			System.out.println("3===>" + random3.nextInt());
		}
		for (int i = 0; i < 10; i++) {
			System.out.println("3===>" + random3.nextInt(10));
		}
	}

	//接口中不要有实现(内部类作为字段的实现)
	//变量要先声明再使用(static反例)
	//静态方法不能重写，静态方法最好用类调用不要用实例调用，实例也是用apparent type

	// 构造函数执行顺序。。子类默认调用父类无参构造函数super(),
	// 执行过程:先分配子类实例变量内存空间赋值0，调用子类构造第0行，
	//   分配父类实例变量空间，初始化父类实例变量，执行父类构造方法内，初始化子类实例变量，执行子类构造方法
	public void test18() {

		class BaseClass1 {
			private int a = 11;

			public BaseClass1() {
				System.out.println(a);
				a = 12;
				System.out.println("BaseClass....");
			}
		}
		class SubClass1 extends BaseClass1 {
			private int b = 13;

			public SubClass1(int a) {
				System.out.println(b);
				b = 14;
				System.out.println("SubClass....");
			}
		}

		new SubClass1(1);
	}

	//构造函数不要太复杂，不要构造其他类，不要调用子类方法，最好很简单，然后开设一个init方法，让外界调用

	//构造代码块会被编译在构造方法第一行
	//功能：初始化实例变量或环境(自动插入,比调用公共方法便捷)
	public void test19() {

		class BaseClass {
			private int a = 11;

			public BaseClass() {
				System.out.println("2222222");
			}

			public BaseClass(int a) {
				System.out.println("3333333");
			}

			public BaseClass(long a) {
				this();//含有this，编译器不会放入构造代码块
				System.out.println("444444");
			}

			{
				System.out.println("1111111");
			}
		}

		class SubClass {
			public SubClass() {
				super();
				System.out.println("55555");
				System.out.println("66666");
			}


			{
				System.out.println("77777");//放到super之后的第一行
			}
		}
	}

	//匿名内部类,没有名称，无构造方法，使用{}作为构造函数
	public void test20() {

		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>() {
		};
		List<String> list3 = new ArrayList<String>() {{
		}};
		List<String> list4 = new ArrayList<String>() {
			{
			}

			{
			}
		};

		//匿名内部类都是新的子类
		System.out.println(list1.getClass() == list2.getClass());
		System.out.println(list1.getClass() == list3.getClass());
		System.out.println(list1.getClass() == list4.getClass());
	}


	//匿名类会调用对应参数的父类构造方法，而不是super()
	public void test21() {

		class BaseClass2 {
			public BaseClass2() {
				System.out.println("2222222");
			}

			public BaseClass2(int a) {
				System.out.println("3333");
			}
		}

		new BaseClass2(1) {
			{
				System.out.println("44444");
			}
		};
	}

	//内部类可以实现多重继承

	//不可实例化的工具类(构造方法抛出异常)
	public void test22() {
		//只能通过类调用
		Util.test();
	}

	//小心java的浅拷贝
	//推荐使用流方式序列化对象拷贝

	//equals注意和自己比较,null判断，使用getClass代替instanceof，自反性，对称性,传递性,推荐使用工具生成
	public void test23() {
		Employee employee1 = new Employee("张三", 100);
		Employee employee2 = new Employee("张三", 1001);
		Person person = new Person("张三");

		System.out.println(person.equals(employee1));
		System.out.println(person.equals(employee2));
		System.out.println(employee1.equals(employee2));

		//class对象在内存中一个类对应一份
		System.out.println(employee1.getClass() == employee2.getClass());
	}

	//对象相同，hashCode必须相同，对象不同，允许相同hashCode
	//重写equals必须也重写hashCode
	public void test24() {
		Person person = new Person("张三");
		Person person1 = new Person("张三");

		List<Person> list = new ArrayList<Person>();
		list.add(person);

		HashMap<Person, Object> map = new HashMap<Person, Object>();
		map.put(person, new Object());

		System.out.println(list.contains(person1));
		//未重写hashCode则使用object的默认本地方法，每个对象都不一样
		System.out.println(map.containsKey(person1));
	}

	//重写toString
	public void test25() {
		Person person = new Person("张三");
		System.out.println(person);
	}

	//不要使用System.gc();

	//使用string直接量赋值
	public void test26() {
		String a = "张三";
		String b = "张三";
		String c = new String("张三");
		String d = c.intern();
		System.out.println(a == b);
		System.out.println(a == c);
		System.out.println(a == d);
		System.out.println(c == d);
	}

	//replace和replaceAll
	public void test27() {
		String a = "张三张";
		System.out.println(a.replaceAll("张", ""));
		String a1 = "$三$";//replaceAll使用正则,$为正则结尾符号
		System.out.println(a1.replaceAll("$", "123"));
		//replace字符替换
		System.out.println(a1.replace("$", "123"));
	}

	//string位置,使用加号时：string后面都会被转换成string
	public void test28() {
		String s2 = 1 + 2 + "张三";
		System.out.println(s2);
		String s1 = "张三" + 1 + 2;
		System.out.println(s1);
		String s3 = "张三" + new ArrayList<String>() {{
			add("aa");
		}};
		System.out.println(s3);
	}

	//使用好正则
	//统一编码

	//数组优于list,变长数组
	public void test29() throws Exception {

		int[] arr = {1, 2, 3, 4};

		int[] ints = Arrays.copyOf(arr, 10);
		for (int anInt : ints) {
			System.out.println(anInt);
		}
	}

	//array shadow copy
	public void test30() throws Exception {

		Person[] arr = {new Person("a"), new Person("b"), new Person("c")};

		Person[] persons = Arrays.copyOf(arr, 10);

		persons[2].setName("xx");

		for (Person person : arr) {
			System.out.println(person.getName());
		}
	}

	//构造集合时给定大小，避免扩容

	//集合提供排序、最值等方法
	public void test31() throws Exception {

		Integer[] arr = {1, 3, 2, 4, 2, 3};

		List<Integer> list = ArrayUtils.toList(arr);
		TreeSet<Integer> ts = new TreeSet<Integer>(list);
		//取倒数第二个
		Integer lower = ts.lower(ts.last());
		System.out.println(lower);

		//此list不可添加
		list.add(66);

		//小心原始类型数组转换成list
		int[] arr1 = {1, 3, 2, 4, 2, 3};
		List<int[]> ints = ArrayUtils.toList(arr1);
	}

	//列表的遍历
	public void test32() throws Exception {

		int stuNum = 80 * 10000;

		List<Integer> list = new ArrayList<Integer>(stuNum);
//		List<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < stuNum; i++) {
			list.add(new Random().nextInt(150));
		}

		long start = System.currentTimeMillis();

		int avg = 0;
		//对于arraylist，此种由于产生iterator，不如下面直接用get
		//对于LinkedList只能使用此方法
//		for (Integer integer : list) {
//			avg+=integer;
//		}
		for (int i = 0; i < stuNum; i++) {
			avg += list.get(i);
		}
		System.out.println("平均分:" + (avg / stuNum));
		System.out.println("执行时间:" + (System.currentTimeMillis() - start));
	}


	//列表的删除和插入使用linkedlist(指针指向)，修改使用arraylist(快速寻找，不用移动元素)

	//sublist(小心返回给别人后，别人误操作)
	public void test33() throws Exception {

		List<String> list = new ArrayList<String>();
		list.add("A");
		list.add("B");

		List<String> list1 = new ArrayList<String>(list);
		System.out.println(list.equals(list1));

		//sublist返回的是源list的视图，操作它会操作源list
		List<String> list2 = list.subList(0, 2);
		list2.add("C");

		System.out.println(list.equals(list1));
		System.out.println(list.equals(list2));
	}

	//binarySearch要求数据有序
	//indexOf用equals而binarySearch用compareTo，两者逻辑应该一样
	//集合的交:retainAll，并:addAll，差:removeAll，不重复交:removeAll addAll

	//hashMap内部要建立entry占用内存，并且扩容是两被增长
	public void test34() throws Exception {
		final Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println(runtime.maxMemory());
				System.out.println(runtime.freeMemory());
				System.out.println(runtime.totalMemory());
			}
		});

		HashMap<String, String> hashMap = new HashMap<String, String>();
		for (int i = 0; i < 400000000; i++) {
			hashMap.put("i" + i, "i" + i + 2);
		}
	}

	//hashMap中的hashCode应避免冲突，查找费时(链表查找)


	//快速失败
	public void test35() throws Exception {

		List<String> list = new ArrayList<String>();
		list.add("A");
		list.add("B");
		list.add("C");
		list.add("D");
		list.add("E");
		list.add("F");

		for (int i = 0; i < list.size(); i++) {
			list.remove(i);
		}
		//ConcurrentModificationException
		//由于remove时modCount++;而iterator的那个expectedModCount没有增加，再next时报错
//		for (String s : list) {
//			list.remove(s);
//		}
	}

	//treeSet是添加元素时排序，后来改变元素不会自动排序，适合不变元素(不变对象，基本类型,string)
	//枚举常量替代接口常量和类常量

	//枚举注意事项
	public void test36(Season season, String name) throws Exception {
		//switch 编译时会调用season.ordinal
		if (null == season) {
			System.out.println("param season is null");
			return;
		}

		//valueOf不匹配时抛出异常
		try {
			Season season1 = Season.valueOf(name);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		switch (season) {
			case SPRING:
				System.out.println(Season.SPRING.desc);
				break;
			case SUMMER:
				System.out.println(Season.SUMMER.desc);
				break;
			case AUTUMN:
				System.out.println(Season.AUTUMN.desc);
				break;
			case WINTER:
				System.out.println(Season.WINTER.desc);
				break;
			//要有默认值，防止后需添加常量，忘记在这里修改
			default:
				System.out.println("xxxxx");
		}
	}

	//枚举相不要超过64个
	public void test37() throws Exception {
		EnumSet<Season> seasons = EnumSet.allOf(Season.class);
		Iterator<Season> iterator = seasons.iterator();
	}

	//@Inherited 注解会被继承到子类上

	//注解+枚举(模拟权限控制)
	public void test38() throws Exception {
		Identifier currentPerson = CommonIdentifier.READER;

		Access access = Foo.class.getAnnotation(Access.class);
		if (null == access || currentPerson.identify(access.level())) {
			System.out.println(CommonIdentifier.REFUSE_WORD);
		}
	}

	//泛型擦除
	public void test39() throws Exception {
		List<String> list1 = new ArrayList<String>();
		List<Integer> list2 = new ArrayList<Integer>();

		//不可以有泛型数组
		Object[] list3 = new Object[4];
		list3[0] = "a";
		System.out.println(list3[0]);

		//instanceof 不允许泛型
//		System.out.println(list1 instanceof List<String>);

		System.out.println(list1.getClass() == list2.getClass());
	}

	//强制声明泛型，一定要是静态方法
	@Test
	public void test40() throws Exception {
		List<Integer> test = Foo.<Integer>test(1, 2, 3);

		//放入的泛型是Number的子类,可以遍历,最高级是Number,能保证父类是Number
		Foo.test1(new ArrayList<Integer>());
		//放入的泛型是Fruit的父类,可以添加,最低级是Apple,能保证最小儿子是Apple
		Foo.test2(new ArrayList<Fruit>());
//		Foo.test2(new ArrayList<RedApple>());//报错
	}

//	“协变”是指能够使用与原始指定的派生类型相比，派生程度更大的类型。
//	“逆变”则是指能够使用派生程度更小的类型。
	//协变就是从小到大,逆变则是从大到小
	public void test41() throws Exception {
		//数组支持协变与逆变
		Number[] nu = new Integer[10];
		Object[] nu1 = nu;
		Integer[] nu11 = (Integer[]) nu1;

        //泛型不支持协变与逆变，只能一模一样。
        // 对于class A<E extends Number>，子类B可以用class B<Integer> extends A，
        // 但是方法methodx(Class<? extends Number>)，子类就不能用methodx(Class<? extends Integer>)重写了
		ArrayList<Integer> test = new ArrayList<Integer>();
		//报错，ArrayList<Number>和ArrayList<Integer>两者没有关系
//		ArrayList<Number> test1 = new ArrayList<Integer>();
//		ArrayList<Number> test2 = test;
	}

	//数组支持协变
	public void test41_2() throws Exception {
		Number[] nu = new Integer[10];
		Object[] nu1 = nu;
	}

	//List<T>,List<?>适用返回,List<Object>不适用
	//泛型多重边界 extends A & B

	public void test42() throws Exception {
		Object[] objects = {"a", "b"};
		//ClassCastException
		String[] integers = (String[]) objects;

		//使用子类型构建，赋值给父类型
		Object[] a = new String[]{"a", "b"};
		String[] xx = (String[]) a;
		System.out.println(xx);


		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");

		// String s = (String)list.get(0);
		String s = list.get(0);

		//ClassCastException
//		String[] strings = toArray(list);

		String[] strings = toArray2(list, String.class);
		System.out.println(strings);
	}

	private static <T> T[] toArray(List<T> list) {
		//编译后：Object[] objects = (Object[])new Object[list.size()];
		T[] objects = (T[]) new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			objects[i] = list.get(i);
		}

		return objects;
	}

	private static <T> T[] toArray2(List<T> list, Class<T> tClass) {
		T[] objects = (T[]) Array.newInstance(tClass, list.size());
		for (int i = 0; i < list.size(); i++) {
			objects[i] = list.get(i);
		}

		return objects;
	}

	//Accessible不是语法层级的权限控制，是jvm层级的控制，表示是否可以快速访问不用检查,true:不用检查,false:检查(默认)
	public void test43() throws Exception {
		Person person = new Person("xx");
		Field field = Person.class.getDeclaredField("name");
		field.setAccessible(true);
		Object o = field.get(person);
		System.out.println(o);
	}

	//forName,array
	public void test44() throws Exception {
		Class<?> aClass = Class.forName("com.car.onefiveone.Person");
		Person person = (Person) aClass.newInstance();
		person.setName("xx");
		System.out.println(person);

		String[] strings = (String[]) Array.newInstance(String.class, 10);
		strings[0] = "a";
		System.out.println(strings[0]);
	}

	//dynamicProxy
	public void test45() throws Exception {
		final Subject realSubject = new RealSubject();
		Class<? extends Subject> aClass = realSubject.getClass();
		Subject subject = (Subject) Proxy.newProxyInstance(aClass.getClassLoader(), aClass.getInterfaces(), new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println("before");
				Object invoke = method.invoke(realSubject, args);
				System.out.println("after");
				return null;
			}
		});
		int request = subject.request();
		System.out.println(request);
	}

	//autobox
	public void test46() throws Exception {
		//compare one
//		Integer i1 = 17;
//		Integer i2 = 17;
//		Integer i3 = 137;
//		Integer i4 = 137;
//		//缓存 -128 -- 127
//		System.out.println(i1 == i2);
//		System.out.println(i3 == i4);

		//compare two
//		Integer i1 = 17;
//		int i2 = 17;
//		int i3 = 137;
//		Integer i4 = 137;
//		//intger和int比较时，自动调用intger.intValue
//		System.out.println(i1 == i2);
//		System.out.println(i3 == i4);

		//compare three
//		Integer i1 = 17;
//		Integer i2 = 17;
//		Integer i3 = 137;
//		Integer i4 = 137;
//		// == 比较两个Integer，比较地址
//		System.out.println(i1 == i2);
//		System.out.println(i3 == i4);
//		// equals,调用intValue比较
//		System.out.println(i1.equals(i2));
//		System.out.println(i3.equals(i4));

	}

	//动态包装类
	public void test47() throws Exception {

		RealSubject realSubject = new RealSubject();
		DynamicDecorator dynamicDecorator1 = new DynamicDecorator(realSubject, Fly.class);
		DynamicDecorator dynamicDecorator2 = new DynamicDecorator(realSubject, Swim.class);

		dynamicDecorator1.request();
		dynamicDecorator2.request();
	}

	//动态模板(反射)
	public void test48() throws Exception {

		Template template = new TemplateImpl();
		template.doSomething();
	}

	//反射子类泛型,泛型用于编译时期，子类已经确定泛型，可以在运行期获得
	public void test49() throws Exception {

		Type[] genericInterfaces = SubFoo.class.getGenericInterfaces();
		System.out.println(genericInterfaces.length);
		ParameterizedType genericSuperclass = (ParameterizedType) SubFoo.class.getGenericSuperclass();
		Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
		System.out.println(actualTypeArguments[0]);
	}

	//对开发和用户进行不同异常封装，达到读懂或者友好展示
	//对异常分别处理，不要catch exception
	//必要时，封装所有异常最后一次返回,例如web注册页面，异常传递
	//受检查异常威胁到安全、正确性等时，需要处理，否则转换成非受检查异常，或者需要调用者处理
	//finally只用来处理收尾工作，不要return
	//构造函数中不要出现异常

	//调用栈信息
	public void test50() throws Exception {

		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		for (StackTraceElement stackTraceElement : stackTrace) {
			System.out.println(stackTraceElement.getMethodName());
		}
	}

	//只是该用异常时采用，不要喧宾夺主，Enum.valueOf()加上catch，捕捉到表示不在枚举范围内，完全可以使用Enum.valus遍历对比
	//多线程，只复写run方法，不要复写start方法
	//想终止一个线程，使用标志位(volatile)或者isInterated。

	//volatile不能保证多线程更新安全，只能保证获取最新值
	public void test52() throws Exception {

		ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();

		System.out.println(threadGroup.activeCount());

	}

	//使用Callable获得结果
	public void test53() throws Exception {

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<String> future = executorService.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				System.out.println("call is running");
				Thread.sleep(5000);
				return "费时间";
			}
		});

		while (!future.isDone()) {
			System.out.println("还没完成");
		}

		System.out.println("完成");

	}

	//线程状态：新建、运行==》阻塞或等待  终结
	//lock可以有读写分离控制(两个锁)，synchronize不行

	//提防死锁问题,使用tryLock避免死锁
	public void test54() throws Exception {
		final B b = new B();
		final A a = new A();
		a.setB(b);
		b.setA(a);

		new Thread(new Runnable() {
			@Override
			public void run() {
				a.test1();
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				b.test1();
			}
		}).start();
	}

	//阻塞队列(ArrayBlockingQueue)的长度固定

	//countDownLatch
	public void test55() throws Exception {
		//10个人准备
		CountDownLatch prepare = new CountDownLatch(10);
		//裁判开枪
		CountDownLatch begin = new CountDownLatch(1);
		//10个人先后到达
		CountDownLatch end = new CountDownLatch(10);

		//10个跑道
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		for (int i = 0; i < 10; i++) {
			list.add(executorService.submit(new Runner(prepare, begin, end)));
			//任务不能马上获取，不然会阻塞,由于runner中begin.await();
//			executorService.submit(new Runner(prepare, begin,end)).get();
		}

		//由于executorService执行runner不一定在main之后，所以控制10个人都到这里才开始比赛
		prepare.await();

		//开始
		begin.countDown();
		System.out.println(" begin match ");
		//等10人都到达
		end.await();

		//都到后计算结果
		int sum = 0;
		for (Future<Integer> integerFuture : list) {
			sum += integerFuture.get();
		}
		System.out.println("final match score is " + sum);

		executorService.shutdown();
	}

	//CyclicBarrier
	public void test56() throws Exception {

		CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new Runnable() {
			@Override
			public void run() {
				System.out.println("都完成。。。");
			}
		});

		new Thread(new Worker(cyclicBarrier, "a")).start();
		new Thread(new Worker(cyclicBarrier, "b")).start();
	}

	//不要在循环条件中计算while(i < count*2){}
	//方法中的变量如果是常量，放到类中去final static
	//缩小变量作用范围
	//Collections.binarySearch非线性搜索,前提要有顺序
	//自动定义异常，Throwable.fillInStackTrace如果不用则覆写之
	//运行程序考虑:cpu、内存、io
	//clone生僻，使用：构造函数复杂，属性多。不一定比new慢
	//正常测试、边界测试、异常测试
	//注释说明意图、概要
	//编码前思考

	//小心+=
	public void test57() throws Exception {
		int a = 1;
		double v = (a + 1) * 2.3;
		a += v;//会自动强转。。
		System.out.println(a);
	}

}

class Util{
	private  Util(){
		throw new RuntimeException("xxx");
	}
	public static void test(){
		System.out.println("11111");
	}
}


class Person{

	public Person() {
	}

	private String name;

	public Person(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (null != obj && obj.getClass() == this.getClass()) {
			Person person = (Person)obj;
			String tempName = person.getName();
			if (tempName == null || name == null) {
				return false;
			}else {
				return name.equals(tempName);
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
class Employee extends Person{
	private int id;

	public Employee(String name, int id) {
		super(name);
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Employee) {
			Employee employee = (Employee)obj;
			return super.equals(employee) && id == employee.getId();
		}
		return false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

enum Season{
	SPRING("春天"),SUMMER("夏天"),AUTUMN("秋天"),WINTER("冬天");

	String desc;
	Season(String desc){
		this.desc= desc;
	}
}

//枚举+注解实现acl(access control )
interface Identifier{
	String REFUSE_WORD="您无权访问";
	public boolean identify(CommonIdentifier a);
}

enum CommonIdentifier implements Identifier{

	READER,AUTHOR,ADMIN;

	@Override
	public boolean identify(CommonIdentifier a) {
		return this.ordinal()<a.ordinal();
	}
}

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@interface Access{
	CommonIdentifier level() default CommonIdentifier.ADMIN;
}

@Access(level = CommonIdentifier.AUTHOR)
class Foo<T,E>{
	//编译时期泛型擦除
//	private T t = new T();
//	private T[] t = new T[4];

	public static  <P> List<P> test(P... p){return null;}

	public static  void test1(List<? extends Number> list) {
		//不能添加，Number是基类，添加任意子类都不合适
//		list.add(new Integer(1));
		for (Number number : list) {
			System.out.println("extends 用于读取");
		}
	}

	public static  void test2(List<? super Apple> list){
		//可以添加，至少是个number类型
//		list.add(1);
//		list.add(1.1);

		List<? super Fruit> flist = new ArrayList<Fruit>();
		flist.add(new Fruit());
		flist.add(new Apple());
		flist.add(new RedApple());

		//super 不能遍历，不知道integer的父类是哪个
//		for (Integer number : list) {
//		}
	}
}

class SubFoo extends Foo<Employee,Person> {
}


class Food{}
class Fruit extends Food{}
class Apple extends Fruit{}
class RedApple extends Apple{}
class Strawberry extends RedApple{}

interface Subject{
	public int request();
}

class RealSubject implements Subject{

	@Override
	public int request() {
		System.out.println("realSubject request ....");
		return 3;
	}
}

interface Behavior{

	public void doOne();
}

class Fly implements Behavior{

	@Override
	public void doOne() {
		System.out.println("fly ...");
	}
}

class Swim implements Behavior{

	@Override
	public void doOne() {
		System.out.println("Swim ...");
	}
}

class DynamicDecorator implements Subject{

	private Subject subject;

	private Behavior behavior;

	public DynamicDecorator(Subject subject, Class<? extends Behavior> behaviorClass) {
		this.subject = subject;
		try {
			this.behavior = behaviorClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int request() {
		behavior.doOne();
		return subject.request();
	}
}

abstract class Template {
	 void doSomething(){
		 Method[] methods = this.getClass().getMethods();
		 for (Method method : methods) {
			 String name = method.getName();
			 if (name.startsWith("init")) {
				 try {
					 method.invoke(this);
				 } catch (Exception e) {
					 e.printStackTrace();
				 }
			 }
		 }
	 }
}

class TemplateImpl extends Template{
	public void initX(){
		System.out.println("xxx");
	}
	public void initY(){
		System.out.println("yyy");
	}
	public void initZ(){
		System.out.println("zzz");
	}
}

class UnSafeThread implements Runnable {

	private volatile int count;

	@Override
	public void run() {
		//增加cpu繁忙程度
		for (int i = 0; i < 1000; i++) {
			Math.hypot(Math.pow(987654321, i), Math.cos(i));
		}

		count++;
	}

	public int getCount() {
		return count;
	}
}

class A{
	private B b;
	private Lock lock = new ReentrantLock();

//	public synchronized void test1(){
//		System.out.println("a.test1");
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		b.test2();
//	}
	public void test1(){

		lock.lock();
		try {
			System.out.println("a.test1");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			b.test2();
		} finally {
			lock.unlock();
		}


	}
//	public synchronized void test2(){
//		System.out.println("a.test2");
//	}

	public void test2(){
		try {
			lock.tryLock(3, TimeUnit.SECONDS);
			System.out.println("a.test2");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}

	public void setB(B b) {
		this.b = b;
	}
}

class B{
	private A a;
	private Lock lock = new ReentrantLock();

//	public synchronized void test1(){
//		System.out.println("b.test1");
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		a.test2();
//	}

	public void test1(){

		lock.lock();
		try {
			System.out.println("b.test1");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			a.test2();
		} finally {
			lock.unlock();
		}
	}
//	public synchronized void test2(){
//		System.out.println("b.test2");
//	}

	public void test2(){

		lock.lock();
		try {
			System.out.println("b.test2");
		} finally {
			lock.unlock();
		}
	}

	public void setA(A a) {
		this.a = a;
	}
}

class Runner implements Callable<Integer>{

	private CountDownLatch begin;
	private CountDownLatch end;
	private CountDownLatch prepare;

	public Runner(CountDownLatch prepare,CountDownLatch begin,CountDownLatch end) {
		this.prepare = prepare;
		this.begin = begin;
		this.end = end;
	}

	@Override
	public Integer call() throws Exception {
		//准备
		Thread.sleep(new Random().nextInt(5000));
		System.out.println(Thread.currentThread().getName() + "prepare ...");
		//准备好
		prepare.countDown();

		//等待开始
		begin.await();

		//比赛中
		System.out.println(Thread.currentThread().getName() + "is running ...");
		Thread.sleep(new Random().nextInt(5000));
		System.out.println(Thread.currentThread().getName() + "is arrived ...");
		//到达
		end.countDown();
		return 1;
	}
}

class Worker implements Runnable{

	private CyclicBarrier barrier;
	private String name;

	public Worker(CyclicBarrier barrier,String name) {
		this.barrier = barrier;
		this.name= name;
	}

	@Override
	public void run() {

		System.out.println("开始挖..."+name);
		try {
			Thread.sleep(new Random().nextInt(5000));
			barrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println("挖完..."+name);
	}
}
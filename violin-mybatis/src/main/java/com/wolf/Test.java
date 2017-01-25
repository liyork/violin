package com.wolf;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2015/6/17
 */
public class Test {

	public static void main(String[] args) throws IOException {
		//mybatis的配置文件
		String resource = "conf.xml";
		//使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
		InputStream is = Test.class.getClassLoader().getResourceAsStream(resource);
		//构建sqlSession的工厂
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
		//使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
		//Reader reader = Resources.getResourceAsReader(resource);
		//构建sqlSession的工厂
		//SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		//创建能执行映射文件中sql的sqlSession
		SqlSession session = sessionFactory.openSession();
		/**
		 * 映射sql的标识字符串，
		 * me.gacl.mapping.userMapper是userMapper.xml文件中mapper标签的namespace属性的值，
		 * getUser是select标签的id属性值，通过select标签的id属性值就可以找到要执行的SQL
		 */
		String statement = "me.gacl.mapping.userMapper.getUser";//映射sql的标识字符串
		//执行查询返回一个唯一user对象的sql
		User user = session.selectOne(statement, 1);
		System.out.println(user);

		//查所有字段
//		String statement = "me.gacl.mapping.userMapper.getAllUser";//映射sql的标识字符串
//		//执行查询返回一个唯一user对象的sql
//		List<User> objects = session.selectList(statement);
//		for (User object : objects) {
//			System.out.println(object);
//		}

		//查部分字段
//		String statement = "me.gacl.mapping.userMapper.getAllUser1";//映射sql的标识字符串
//		//执行查询返回一个唯一user对象的sql
//		List<User> objects = session.selectList(statement);
//		for (User object : objects) {
//			System.out.println(object);
//		}

		//单表插入
//		User user = new User();
//		user.setName("name1");
//		user.setAge(50);
//		session.insert("me.gacl.mapping.userMapper.insertUser", user);
//		session.commit();
//		System.out.println(user.getId());


//		关联插入
//		User user = new User();
//		user.setName("name1");
//		user.setAge(50);
//		session.insert("me.gacl.mapping.userMapper.insertUser", user);
//
//		UserRef userRef = new UserRef();
//		userRef.setName("name1");
//		userRef.setUserid(user.getId());
//		session.insert("me.gacl.mapping.userMapper.insertUserRef", userRef);
//		session.commit();
//		session.close();
//		System.out.println(userRef.getId());


		//更新
//		User user = new User();
//		user.setId(9);
//		user.setName("name99");
//		session.update("me.gacl.mapping.userMapper.updateUser",user);
//		session.commit();
//		session.close();

		//删除
//		User user = new User();
//		user.setId(10);
//		session.delete("me.gacl.mapping.userMapper.deleteUser",user);
//		session.commit();
//		session.close();


//		String statement = "me.gacl.mapping.orderMapper.getAllOrder1";//映射sql的标识字符串
//		String statement = "me.gacl.mapping.orderMapper.getAllOrder2";//映射sql的标识字符串
//		//执行查询返回一个唯一user对象的sql
//		List<Order> objects = session.selectList(statement);
//		for (Order object : objects) {
//			System.out.println(object);
//		}


//		String statement = "me.gacl.mapping.classMapper.getClass1";//映射sql的标识字符串
//		//执行查询返回一个唯一user对象的sql
//		List<Class> objects = session.selectList(statement,0);//查询全部
////		List<Class> objects = session.selectList(statement,1);//查询一个
//		for (Class object : objects) {
//			System.out.println(object);
//		}


////		================================测试一级缓存
//		String statement = "me.gacl.mapping.userMapper.getUser";//映射sql的标识字符串
//		//执行查询返回一个唯一user对象的sql
//		User user1 = session.selectOne(statement, 1);
//		System.out.println(user1.hashCode());
////		session.clearCache();
//
//		//session执行c/u/d都会清空select缓存数据
//		user1.setName("33");
//		session.update("me.gacl.mapping.userMapper.updateUser",user1);
//		session.commit();
//
//		//执行查询返回一个唯一user对象的sql
//		User user2 = session.selectOne(statement, 1);
//		System.out.println(user2.hashCode());
////		session.clearCache();
//		User user4 = session.selectOne(statement, 3);
//		System.out.println(user4.hashCode());
//		User user3 = session.selectOne(statement, 1);
//		System.out.println(user3.hashCode());


		//两个session，一个session修改不能及时通知另一个，那么需要设定<select>标签的useCache="false" flushCache="true"属性
//		User user2 = session.selectOne(statement, 1);
//		System.out.println(user2.hashCode()+"__"+user2.getName());
//
//		User user = new User();
//		user.setId(user2.getId());
//		user.setName("qqq18");
//		SqlSession session2 = sessionFactory.openSession(false);
//		session2.update("me.gacl.mapping.userMapper.updateUser", user);
//		session2.commit();
//		session2.close();
//
//		User user3 = session.selectOne(statement, 1);
//		System.out.println(user3.hashCode()+"__"+user3.getName());



		//=============================测试条件标签,动态sql
//		if
//		String statement = "me.gacl.mapping.userMapper.getIfUser";//映射sql的标识字符串
//		//执行查询返回一个唯一user对象的sql
//		User user1 =new User();
//		user1.setName("");//user1.setName("33");//一样效果都不为null
////		user1.setName(null);
//
//		List<User> user = session.selectList(statement, user1);
//
//		System.out.println(user.size());



		//		choose
//		String statement = "me.gacl.mapping.userMapper.getChooseUser";//映射sql的标识字符串
//		//执行查询返回一个唯一user对象的sql
//		User user1 =new User();
//		user1.setName("44");
//		user1.setAge(22);
//
//		List<User> user = session.selectList(statement, user1);
//
//		System.out.println(user.size());


		//		where
//		String statement = "me.gacl.mapping.userMapper.getWhereUser";//映射sql的标识字符串
//
//		List<User> user = session.selectList(statement);
//
//		System.out.println(user.size());


		//trim
//		String statement = "me.gacl.mapping.userMapper.getTrimUser";//映射sql的标识字符串
//		User user1 =new User();
//		user1.setName("33");
//		user1.setAge(11);
//		List<User> user = session.selectList(statement,user1);
//
//		System.out.println(user.size());


		//for each list
//		String statement = "me.gacl.mapping.userMapper.getForEachListUser";//映射sql的标识字符串
//		List<User> users =new ArrayList<User>();
//		User user1  = new User();
//		user1.setId(1);
//		User user2  = new User();
//		user2.setId(2);
//		users.add(user1);
//		users.add(user2);
//
//		List<User> user = session.selectList(statement,users);
//
//		System.out.println(user.size());


//		//for each array
//		String statement = "me.gacl.mapping.userMapper.getForEachArrayUser";//映射sql的标识字符串
//		User user1  = new User();
//		user1.setId(1);
//		User user2  = new User();
//		user2.setId(2);
//		User[] users = {user1,user2};
//
//		List<User> user = session.selectList(statement,users);
//
//		System.out.println(user.size());


		//for each map
//		String statement = "me.gacl.mapping.userMapper.getForEachMapUser";//映射sql的标识字符串
//		List<User> users =new ArrayList<User>();
//		User user1  = new User();
//		user1.setId(1);
//		User user2  = new User();
//		user2.setId(2);
//		users.add(user1);
//		users.add(user2);
//
//		Map<String,List<User>> map = new HashMap<String, List<User>>();
//		map.put("users", users);
//		map.put("title", null);
//
//		List<User> user = session.selectList(statement,map);
//
//		System.out.println(user.size());


		//=============================分页
//		//物理分页
//		PageHelper.startPage(2,3);
//		String statement = "me.gacl.mapping.userMapper.getAllUser";//映射sql的标识字符串
//		//执行查询返回一个唯一user对象的sql
//		List<User> objects = session.selectList(statement);
//		for (User object : objects) {
//			System.out.println(object);
//		}
//		PageHelper.Page page = PageHelper.endPage();
//		System.out.println(page.getResult().size());


		//逻辑分页
//		PageHelper.startPage(2,3);
//		String statement = "me.gacl.mapping.userMapper.getAllUser";//映射sql的标识字符串
//		//执行查询返回一个唯一user对象的sql
//		List<User> objects = session.selectList(statement,"",new RowBounds(1,3));
//		for (User object : objects) {
//			System.out.println(object);
//		}
//		PageHelper.Page page = PageHelper.endPage();
//		System.out.println(page.getResult().size());





	}
}

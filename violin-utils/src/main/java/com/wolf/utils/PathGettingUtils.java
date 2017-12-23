package com.wolf.utils;

import java.io.InputStream;
import java.net.URL;

/**
 * Description:
 * class.getClassLoader().getResource不能已/开头,表明从classpath下查找
 * class.getResource()如果已/开头表示classpath下查找
 * class.getResource()如果不已/开头表示从当前类所在包查找
 * getServletConfig().getServletContext().getResourceAsStream("/WEB-INF/classes/" + fileName)从/webroot下的/WEB-INF/classes/查找
 * <p>
 * <br/> Created on 2016/12/6 10:54
 *
 * @author 李超()
 * @since 1.0.0
 */
public class PathGettingUtils {

	//=================classpath root start
	public static URL getClassPathRoot1(Class clazz) {
		//classloader的当前路径("")就是项目根路径
		//System.out.println(clazz.getClassLoader().getResource("").getPath());
//		System.out.println(clazz.getClassLoader().getResource("/"));
		//类的当前路径是当前类所在路径
		//System.out.println(clazz.getResource("").getPath());
		//类的根路径就是项目的根路径
		//System.out.println(clazz.getResource("/").getPath());

		return PathGettingUtils.class.getClassLoader().getResource("");
	}

	public static URL getClassPathRoot2() {
		return PathGettingUtils.class.getResource("/");
	}

	//==================package path start
	public static String getPackagePath(Class clazz) {
		return clazz.getResource("").getPath();
	}

	//==================resource from class path start
	public static URL getResourceFromClassPath(Class clazz,String path) {
		return clazz.getClassLoader().getResource(path);
	}
	public static URL getResourceFromClassPathStartWithSlash(String path) {
		return PathGettingUtils.class.getResource(path);
	}

	//==================input stream from class path start
	public static InputStream getStreamFromClassPath(String path) {
		return PathGettingUtils.class.getClassLoader().getResourceAsStream(path);
	}
	public static InputStream getStreamFromClassPathStartWithSlash(String path) {
		return PathGettingUtils.class.getResourceAsStream(path);
	}
}

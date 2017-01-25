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
public class ResourceUtils {

	//=================classpath root start
	public static URL getClassPathRoot1() {
		return ResourceUtils.class.getClassLoader().getResource("");
	}

	public static URL getClassPathRoot2() {
		return ResourceUtils.class.getResource("/");
	}

	//==================package path start
	public static String getPackagePath(Class clazz) {
		return clazz.getResource("").getPath();
	}

	//==================resource from class path start
	public static URL getResourceFromClassPath(String path) {
		return ResourceUtils.class.getClassLoader().getResource(path);
	}
	public static URL getResourceFromClassPathStartWithSlash(String path) {
		return ResourceUtils.class.getResource(path);
	}

	//==================input stream from class path start
	public static InputStream getStreamFromClassPath(String path) {
		return ResourceUtils.class.getClassLoader().getResourceAsStream(path);
	}
	public static InputStream getStreamFromClassPathStartWithSlash(String path) {
		return ResourceUtils.class.getResourceAsStream(path);
	}
}

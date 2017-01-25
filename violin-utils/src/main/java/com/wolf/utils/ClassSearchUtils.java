package com.wolf.utils;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p> Description:查找特定资源
 * <p/>
 * Date: 2016/6/14
 * Time: 13:29
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ClassSearchUtils {

	/**
	 * 获得包下面的所有的class
	 *
	 * @param pack
	 *            package完整名称
	 * @return List包含所有class的实例
	 */
	public static List<String> getClassesFromPackage(String pack) {
		List<String> clazzs = new ArrayList<String>();

		// 是否循环搜索子包
		boolean recursive = true;

		// 包名字
		// 包名对应的路径名称
		String packageDirName = pack.replace('.', '/');

		Enumeration<URL> dirs;

		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();

				String protocol = url.getProtocol();

				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findClassInPackageByFile(pack, filePath, recursive, clazzs);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clazzs;
	}

	/**
	 * 在package对应的路径下找到所有的class
	 *
	 * @param packageName
	 *            package名称
	 * @param filePath
	 *            package对应的路径
	 * @param recursive
	 *            是否查找子package
	 * @param clazzs
	 *            classname集合
	 */
	public static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive, List<String> clazzs) {
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 在给定的目录下找到所有的文件，并且进行条件过滤
		File[] dirFiles = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
				boolean acceptClass = file.getName().endsWith("class");// 接受class文件
				return acceptDir || acceptClass;
			}
		});

		for (File file : dirFiles) {
			if (file.isDirectory()) {
				findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				clazzs.add(packageName + "." + className);
			}
		}
	}


	/**
	 * 从jar文件中读取指定目录下面的所有的class文件
	 *
	 * @param jarPath
	 *            jar文件存放的位置
	 * @param packageName
	 *            指定的包名
	 * @return 所有的的class名称
	 */
	public static List<String> getClassesFromJarFile(String jarPath, String packageName) {
		List<String> classes = new ArrayList<String>();

		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jarPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String packagePath = packageName.replace(".", "/");

		List<JarEntry> jarEntryList = new ArrayList<JarEntry>();

		Enumeration<JarEntry> ee = jarFile.entries();
		while (ee.hasMoreElements()) {
			JarEntry entry = ee.nextElement();
			// 已指定包开头，已.class结尾，JarEntry有可能表示包
			if (entry.getName().startsWith(packagePath) && entry.getName().endsWith(".class")) {
				jarEntryList.add(entry);
			}
		}
		for (JarEntry entry : jarEntryList) {
			String className = entry.getName().replace('/', '.');
			classes.add(className.substring(0, className.length() - 6));
		}

		return classes;
	}

	/**
	 * 通过流将jar中的一个文件的内容输出
	 *
	 * @param jarPath
	 *            jar文件存放的位置
	 * @param packageName
	 *            指定的文件目录
	 */
	public static void readFileFromJar(String jarPath, String fileName) {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jarPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Enumeration<JarEntry> ee = jarFile.entries();

		List<JarEntry> jarEntryList = new ArrayList<JarEntry>();
		while (ee.hasMoreElements()) {
			JarEntry entry = ee.nextElement();
			// 过滤我们出满足我们需求的东西，这里的fileName是指向一个具体的文件的对象的完整包路径，比如com/mypackage/test.txt
			System.out.println(entry.getName());
			if (entry.getName().endsWith(fileName)) {
				jarEntryList.add(entry);
			}
		}
		try {
			InputStream in = jarFile.getInputStream(jarEntryList.get(0));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String s = "";

			while ((s = br.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//使用用例
	public static void main(String[] args) {
//		List<String> classes = ClassUtils.getClassesFromPackage("com.car.designpattern.creational.factory");
//		for (String className : classes) {
//			System.out.println(className);
//		}

//		List<String> classNames = getClassesFromJarFile("E:\\intellijRepository\\com\\xx\\xxbase\\xxbase\\1.9.35\\xxbase-1.9.35.jar", "com.xx.base.alarminfo.alarm");
//		for (String className : classNames) {
//			System.out.println(className);
//		}

		readFileFromJar("E:\\intellijRepository\\com\\xx\\xxbase\\xxbase\\1.9.35\\xxbase-1.9.35.jar", "FlightInfoResultDTO.class");
	}
}

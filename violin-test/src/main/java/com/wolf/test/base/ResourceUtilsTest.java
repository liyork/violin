package com.wolf.test.base;

import com.wolf.utils.ResourceUtils;
import com.wolf.utils.log.CustomLoggerFactory;
import org.junit.Test;

import java.net.URL;

/**
 * Description:
 * <br/> Created on 2016/12/30 1:01
 *
 * @author 李超()
 * @since 1.0.0
 */
public class ResourceUtilsTest {

	//  /D:/intellijWrkSpace/violin/violin-utils/target/classes/
	@Test
	public void testGetClassPath() {
		URL url1 = ResourceUtils.getClassPathRoot1();
		URL url2 = ResourceUtils.getClassPathRoot2();
		System.out.println(url1.getPath().equals(url2.getPath()));
	}

	@Test
	public void testGetPackagePath() {
		System.out.println(ResourceUtils.getPackagePath(CustomLoggerFactory.class));
	}

	@Test
	public void testGetResourceFromClassPath() {
		URL url1 = ResourceUtils.getResourceFromClassPath("com/wolf/utils");
		System.out.println(url1.getPath());
		URL url2 = ResourceUtils.getResourceFromClassPath("com/wolf/utils/");
		System.out.println(url2.getPath());
		URL url3 = ResourceUtils.getResourceFromClassPath("com/wolf/utils/JsonUtils.class");
		System.out.println(url3.getPath());
		URL url4 = ResourceUtils.getResourceFromClassPath("ftp.properties");
		System.out.println(url4.getPath());
	}

	@Test
	public void testGetResourceFromClassPathBeginSlash() {
		URL url1 = ResourceUtils.getResourceFromClassPathStartWithSlash("/com/wolf/utils");
		System.out.println(url1.getPath());
		URL url2 = ResourceUtils.getResourceFromClassPathStartWithSlash("/com/wolf/utils/");
		System.out.println(url2.getPath());
		URL url3 = ResourceUtils.getResourceFromClassPathStartWithSlash("/com/wolf/utils/JsonUtils.class");
		System.out.println(url3.getPath());
		URL url4 = ResourceUtils.getResourceFromClassPathStartWithSlash("/ftp.properties");
		System.out.println(url4.getPath());
	}
}

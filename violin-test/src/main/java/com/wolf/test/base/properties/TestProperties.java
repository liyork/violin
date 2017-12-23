package com.wolf.test.base.properties;

import com.wolf.utils.PathGettingUtils;
import org.junit.Test;

import java.io.InputStream;
import java.util.Properties;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2015/6/19
 */
public class TestProperties {

	//这里暂时本地测试不了，由于需要两者在一个工程中，或者util以lib形式出现，才可获得资源
	@Test
	public void testBaseFunction() throws Exception {
		InputStream resourceAsStream = PathGettingUtils.getStreamFromClassPathStartWithSlash("/com/wolf/test/base/properties/xx.properties");
		Properties properties = new Properties();
		properties.load(resourceAsStream);
		System.out.println(properties.get("name"));

		InputStream resourceAsStream1 = PathGettingUtils.getStreamFromClassPathStartWithSlash("/com/wolf/test/base/properties/yy.properties");
		Properties properties1 = new Properties();
		properties1.load(resourceAsStream1);
		System.out.println(properties1.get("qq"));

	}
}

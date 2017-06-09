package com.wolf.test.upload;

import com.wolf.utils.UploadUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/27
 * Time: 15:19
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class TestUpload {

	public static void main(String[] args) throws Exception {
		List<InputStream> list = new ArrayList<>();
		list.add(new FileInputStream(new File("E:\\baidu.jpg")));
		String fileNames[] = new String[1];
		fileNames[0] = "baidu123.png";
		UploadUtils.uploadInputStream(list, fileNames, 21);
	}
}

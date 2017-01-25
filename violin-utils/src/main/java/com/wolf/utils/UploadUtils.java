package com.wolf.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/27
 * Time: 15:06
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class UploadUtils {

	private static final Logger logger = LoggerFactory.getLogger(UploadUtils.class);
	public static List<String> uploadInputStream(List<InputStream> inputStreams, String[] fileNames,int uploadType) throws Exception {
		List<String> filePathList = new ArrayList<String>();
		FtpUtils ftpUtils = FtpUtils.getInstance();
		try{
			ftpUtils.connectServer();
			for (int i = 0; i < inputStreams.size(); i++) {
				StringBuilder sb = new StringBuilder();
				String fileName = String.valueOf(UUID.randomUUID()) +"-" + fileNames[i];
				logger.error("path:"+getUploadPath(uploadType)+" fileName:"+fileName+"  ");

				boolean flag = ftpUtils.upload(getUploadPath(uploadType), fileName, inputStreams.get(i));
				try {
					inputStreams.get(i).close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (!flag) {
					throw new RuntimeException("文件上传出错!");
				}

				sb.append(FtpUtils.REMOTE_PATH).append(getUploadPath(uploadType)).append(fileName);

				filePathList.add(sb.toString());
			}
		}finally {
			ftpUtils.closeConnect();
		}
		return filePathList;
	}

	private static String getUploadPath(int type) {
		String fileDir = FtpUtils.getRemoteFileDir(type);
		while (fileDir.startsWith("/")) {
			fileDir = fileDir.substring(1);
		}
		if(!fileDir.endsWith("/")) {
			fileDir += "/";
		}
		return fileDir;
	}
}

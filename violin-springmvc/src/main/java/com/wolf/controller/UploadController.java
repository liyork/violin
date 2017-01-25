package com.wolf.controller;

import com.wolf.utils.FileUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <br/> Created on 2016/12/15 8:37
 *
 * @author 李超()
 * @since 1.0.0
 */
@Controller
@RequestMapping("/file")
public class UploadController {

	@RequestMapping("/upload1")
	public String upload1(@RequestParam("file") CommonsMultipartFile[] files, HttpServletRequest request) {

		System.out.println(request.getParameter("xx"));

		for (CommonsMultipartFile file : files) {
			System.out.println("fileName---------->" + file.getOriginalFilename());

			if (!file.isEmpty()) {
				int pre = (int) System.currentTimeMillis();
				try {
					//拿到输出流，同时重命名上传的文件
					String fileName = "E:/upload/" + new Date().getTime() + file.getOriginalFilename();
					//拿到上传文件的输入流
					ByteArrayInputStream in = (ByteArrayInputStream) file.getInputStream();
					FileUtils.writeFile(fileName, in);
					int finalTime = (int) System.currentTimeMillis();
					System.out.println(finalTime - pre);

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("上传出错");
				}
			}
		}
		return "/succ";
	}


	@RequestMapping("/upload2")
	public String upload2(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		//判断 request 是否有文件上传,即多部分请求
		if (ServletFileUpload.isMultipartContent(request)) {
			//转换成多部分request
			MultipartRequest multiRequest = (MultipartRequest) request;
			Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
			for (Map.Entry<String, MultipartFile> multipartFiles : fileMap.entrySet()) {
				//记录上传过程起始时的时间，用来计算上传时间
				int pre = (int) System.currentTimeMillis();
				//取得上传文件
				MultipartFile file = multipartFiles.getValue();
				if (file != null) {
					//取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					//如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if (!myFileName.trim().equals("")) {
						System.out.println(myFileName);
						//重命名上传后的文件名
						String fileName = "demoUpload" + file.getOriginalFilename();
						//定义上传路径
						String path = "E:/upload/" + fileName;
						File localFile = new File(path);
						file.transferTo(localFile);
					}
				}
				//记录上传该文件后的时间
				int finalTime = (int) System.currentTimeMillis();
				System.out.println(finalTime - pre);
			}

		}
		return "/succ";
	}

	@RequestMapping("/upload3")
	public String upload3(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		//判断 request 是否有文件上传,即多部分请求
		if (ServletFileUpload.isMultipartContent(request)) {
			//转换成多部分request
			MultipartRequest multiRequest = (MultipartRequest) request;
			MultiValueMap<String, MultipartFile> multiFileMap = multiRequest.getMultiFileMap();
			for (Map.Entry<String, List<MultipartFile>> entry : multiFileMap.entrySet()) {
				List<MultipartFile> value = entry.getValue();
				for (MultipartFile multipartFile : value) {
					//记录上传过程起始时的时间，用来计算上传时间
					int pre = (int) System.currentTimeMillis();
					//取得上传文件
					if (multipartFile != null) {
						//取得当前上传文件的文件名称
						String myFileName = multipartFile.getOriginalFilename();
						//如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (!myFileName.trim().equals("")) {
							System.out.println(myFileName);
							//重命名上传后的文件名
							String fileName = "demoUpload" + multipartFile.getOriginalFilename();
							//定义上传路径
							String path = "E:/upload/" + fileName;
							File localFile = new File(path);
							multipartFile.transferTo(localFile);
						}
					}
					//记录上传该文件后的时间
					int finalTime = (int) System.currentTimeMillis();
					System.out.println(finalTime - pre);
				}
			}

		}
		return "/succ";
	}
}

package com.wolf.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description:Servlet能动态显示网页内容
 * <br/> Created on 2016/12/6 8:43
 *
 * @author 李超()
 * @since 1.0.0
 */
public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = -1915463532411657451L;

	@Override
	protected void doGet(HttpServletRequest request,
						 HttpServletResponse response) throws ServletException, IOException {
		downloadFile(request, response, "download.txt");
	}

	//下载就是设定contenttype和header，然后输出流
	private void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {

		ServletOutputStream outStream = response.getOutputStream();
		ServletContext context = getServletConfig().getServletContext();

		//根据文件名获取mimetype
		String mimeType = context.getMimeType(fileName);
		response.setContentType(mimeType != null ? mimeType : "text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

		//从webroot下查找，已/开头
		InputStream in = context.getResourceAsStream("/WEB-INF/classes/" + fileName);

		final int BYTES = 1024;
		int readLength;
		byte[] buffer = new byte[BYTES];

		while ((in != null) && ((readLength = in.read(buffer)) != -1)) {
			outStream.write(buffer, 0, readLength);
		}

		outStream.flush();
		outStream.close();
	}
}

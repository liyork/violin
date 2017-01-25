package com.wolf.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.LastModified;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 * <br/> Created on 2016/12/14 13:54
 *
 * @author 李超()
 * @since 1.0.0
 */
public class DemoLastModifiedController extends AbstractController implements LastModified {
	private long lastModified;

	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//点击后再次请求当前页面
		resp.getWriter().write("<a href=''>this</a>");
		return null;
	}

	public long getLastModified(HttpServletRequest request) {
		if (lastModified == 0L) {
			//此处更新的条件：如果内容有更新，应该重新返回内容最新修改的时间戳
			lastModified = System.currentTimeMillis();
		}
		return lastModified;
	}
}
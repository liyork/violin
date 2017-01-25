package com.wolf.httpservletbean;

import org.springframework.web.servlet.HttpServletBean;

/**
 * Description:
 * <br/> Created on 2016/11/14 17:37
 *
 * @author 李超()
 * @since 1.0.0
 */
public class MyHttpServletBean extends HttpServletBean {

	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

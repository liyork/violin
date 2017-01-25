package com.wolf.test.spring.beanwrapper;

import java.util.Map;

/**
 * Description:
 * <br/> Created on 2016/11/9 13:51
 *
 * @author 李超()
 * @since 1.0.0
 */
class Company extends Parent {

	private String companyName;
	private Map<String, String> attrs;
	private String id;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Map<String, String> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, String> attrs) {
		this.attrs = attrs;
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}
}

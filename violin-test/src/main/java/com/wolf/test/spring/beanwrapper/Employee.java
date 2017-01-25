package com.wolf.test.spring.beanwrapper;

import java.util.List;

/**
 * Description:
 * <br/> Created on 2016/11/9 13:51
 *
 * @author 李超()
 * @since 1.0.0
 */
public class Employee {

	private Company company;
	private String name;
	private String id;
	private List<Company> companies;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}
}

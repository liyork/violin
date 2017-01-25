package com.wolf.test.spring.factoryBean;

/**
 * Description:
 * <br/> Created on 2016/10/20 13:26
 *
 * @author 李超()
 * @since 1.0.0
 */
public class Car {
	private int maxSpeed;
	private String brand;
	private double price;
	public int getMaxSpeed() {
		return this.maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public String getBrand() {
		return this.brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public double getPrice() {
		return this.price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}

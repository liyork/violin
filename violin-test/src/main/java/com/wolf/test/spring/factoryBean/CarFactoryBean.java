package com.wolf.test.spring.factoryBean;

import org.springframework.beans.factory.FactoryBean;

/**
 * Description:
 * <br/> Created on 2016/10/20 13:26
 *
 * @author 李超()
 * @since 1.0.0
 */
public class CarFactoryBean implements FactoryBean<Car> {
	private String carInfo;
	public Car getObject() throws Exception {
		Car car = new Car();
		String[] infos = carInfo.split(",");
		car.setBrand(infos[0]);
		car.setMaxSpeed(Integer.valueOf(infos[1]));
		car.setPrice(Double.valueOf(infos[2]));
		return car;
	}
	public Class<Car> getObjectType() {
		return Car.class;
	}
	public boolean isSingleton() {
		return false;
	}
	public String getCarInfo() {
		return this.carInfo;
	}
	// 接受逗号分割符设置属性信息
	public void setCarInfo(String carInfo) {
		this.carInfo = carInfo;
	}
}

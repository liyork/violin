package com.wolf.test.spring.sourcetest;

/**
 * Description:
 * <br/> Created on 2016/11/7 9:33
 *
 * @author 李超()
 * @since 1.0.0
 */
public class D extends C{

	private B b ;

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}

	//测试MethodOverride，没成功
	@Override
	public void test(){
		System.out.println("dddddd");
	}
}

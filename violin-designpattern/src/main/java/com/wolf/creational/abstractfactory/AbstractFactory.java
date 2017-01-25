package com.wolf.creational.abstractfactory;

/**
 * <p> Description:
 * 有多个产品树：
 * 		宝马：1系，2系。。。
 * 		奔驰：1系，2系。。。
 * 要创建多个产品族
 * 		1系族(宝马奔驰)，2系族(宝马奔驰)
 * 	应该是按照产品族使用场景，即：需要一个工厂产生一个系的所有车，如果一个树的产品则使用factory method
 * <p/>
 * Date: 2016/6/14
 * Time: 16:55
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface AbstractFactory {

	public BaoMa createBaoMa();
	public BenChi createBenChi();
}

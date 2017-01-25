package com.wolf.creational.abstractfactory;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/14
 * Time: 16:56
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ConcreteAbstractFactory1 implements AbstractFactory {
	@Override
	public BaoMa createBaoMa() {
		return new ConcreteBaoMa1Xi();
	}

	@Override
	public BenChi createBenChi() {
		return new ConcreteBenChi1Xi();
	}
}

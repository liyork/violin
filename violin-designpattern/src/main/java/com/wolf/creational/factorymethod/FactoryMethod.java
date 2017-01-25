package com.wolf.creational.factorymethod;

/**
 * <p> Description:用来封装创建对象的。
 * 运行时才知道如何创建产品对象。
 * it defines an interface for creating an object,
 * but leaves the choice of its type to the subclasses, creation being deferred at run-time
 * 对于一个产品族使用工厂方法(定义工厂接口返回值是产品族顶端接口，各个子类实现方法返回不同产品)
 * <p/>
 * Date: 2016/6/14
 * Time: 16:55
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface FactoryMethod {

	public Product createProduct();
}

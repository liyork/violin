package com.wolf.test.base.serialize;

import java.io.*;

/**
 * <p> Description: 如果没有显示声明serialVersionUID，jvm编译时自动产生随机号(根据包名、字段等)，
 * 如果反序列化中serialVersionUID与对应的class文件中的uid不一样就会导致下面错误。。。
 *
 * Exception in thread "main" java.io.InvalidClassException:
 * com.xx.xx.intrDriver.serviceComs.XX; local class incompatible:
 * stream classdesc serialVersionUID = -8601175111683246325, local class serialVersionUID = -8431080549510983752
 *
 * 所以如果是远程访问这种，当升级一个对象，添加一个字段，如果使用默认 ，就不是一样的uid，所以应该显示声明
 * <p/>
 * Date: 2015/7/20
 * Time: 9:04
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class SerializeObject implements Serializable {

	private static final long serialVersionUID = -8833714323168125858L;

	//常量会在反序列化是重新计算(调用方法除外)，即序列化时是1，反序列化时class改成2，则最终就是2
	public final String CONSTANT = "2";

	//单例情况，静态内部类能保证多线程访问时序性
	private static class InstanceHolder {
		private static final SerializeObject instatnce = new SerializeObject(1, "31",4.3f,"25500");
	}

	public SerializeObject(){

	}

	public SerializeObject(int id, String orderNo, float price, String carNo) {
		this.id = id;
		this.orderNo = orderNo;
		this.price = price;
		this.carNo = carNo;
	}

	public static SerializeObject getInstance(){
		return InstanceHolder.instatnce;
	}

	//Order实体类中属性名和orders表中的字段名是不一样的
	private int id;                //id===>order_id
	private String orderNo;        //orderNo===>order_no
	private float price;        //price===>order_price

	//transient变量不被序列化和反序列化
	transient private String carNo;

	//静态变量属于类所有，非成员变量，序列化只关心成员变量
	public static int count = 3;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	//TODO 此时测试这两个方法只支持java序列化，hessian不支持
	//writeObject()与readObject()都是private方法，那么它们是如何被调用的呢？毫无疑问，是使用反射。
	// 详情可以看看ObjectOutputStream中的writeSerialData方法，以及ObjectInputStream中的readSerialData方法
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
//		other thing;
		//手动设定carNo
		out.writeUTF(carNo);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
//		other thing;
		carNo = in.readUTF();
	}


	private Object readResolve() throws ObjectStreamException {
		return InstanceHolder.instatnce;
	}


	@Override
	public String toString() {
		return "Order [id=" + id + ", orderNo=" + orderNo + ", price=" + price + ", carNo="+carNo+", count="+count+"]";
	}


}

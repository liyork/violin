package com.wolf.test.thread.threadlocal;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/23
 * Time: 11:50
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
class Person{
	private String name;
	private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>(){
		@Override
		protected Integer initialValue() {
			return 0;
		}
	};
	private Integer count = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//此处线程有安全问题，虽然使用threadLocal，但是它只是将栈中数据本地拷贝一份到线程中去，但是count++时会一起操作栈中数据，导致问题。
	void increase(){

		this.count = threadLocal.get() + 1;
		System.out.println("1111");//这段如果去掉，可能由于cpu太快，导致下面的++不会出现线程问题，
		count++;
		threadLocal.set(count);
	}

	int getAge(){
		return threadLocal.get();
	}

	public Integer getCount() {
		return count;
	}
}
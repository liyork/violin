package com.wolf.behavioral.observe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/16
 */
public abstract class AbstractClockSetting implements Subject {

	protected Date currentDate;

	private List<Observer> list = new ArrayList<Observer>();

	@Override
	public void attach(Observer observer) {
		list.add(observer);
	}

	@Override
	public void detach(Observer observer) {
		list.remove(observer);
	}

	@Override
	public void notifyObserver() {
		for (Observer observer : list) {
			observer.update(this);
		}
	}

	// 自一致性，一定保证所有操作后再notify,用模板方法,防止子类勿用
	public void tick() {
		setDate();
		notifyObserver();
	}

	protected abstract void setDate();

	public Date getCurrentDate(){
		return currentDate;
	}
}

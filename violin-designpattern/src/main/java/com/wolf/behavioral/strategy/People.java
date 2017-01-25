package com.wolf.behavioral.strategy;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/17
 */
public class People {

	//模拟strategy需要用到context中内容
	private String prefix = "go to Work ";

	private TripMode tripMode;

	public void goToWork(){
		this.tripMode.toWork(this);
	}

	public TripMode getTripMode() {
		return tripMode;
	}

	public void setTripMode(TripMode tripMode) {
		this.tripMode = tripMode;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}

package com.wolf.behavioral.observe;

import java.util.Date;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/15
 */
public class AnalogClock implements Widget,Observer {

	private Date currentDate;
	@Override
	public void update(Subject subject) {
		if (subject instanceof AutomaticClockSetting) {
			AutomaticClockSetting clockTimer = (AutomaticClockSetting) subject;
			System.out.println("update in AnalogClock by AutomaticClockSetting");
			currentDate = clockTimer.getCurrentDate();
			draw();
		}else if (subject instanceof ManualClockSetting) {
			ManualClockSetting clockTimer = (ManualClockSetting) subject;
			System.out.println("update in AnalogClock by ManualClockSetting");
			currentDate = clockTimer.getCurrentDate();
			draw();
		}
	}

	@Override
	public void draw() {
		System.out.println("current date is "+currentDate.toString());
	}
}

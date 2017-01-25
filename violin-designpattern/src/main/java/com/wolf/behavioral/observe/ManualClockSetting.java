package com.wolf.behavioral.observe;

import java.util.Calendar;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/15
 */
public class ManualClockSetting extends AbstractClockSetting {

	@Override
	public void setDate(){
		Calendar calendar = Calendar.getInstance();
		//由于是手动的模拟真实情况，比自动慢一分钟
		calendar.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE)-1);
		this.currentDate = calendar.getTime();
	}
}

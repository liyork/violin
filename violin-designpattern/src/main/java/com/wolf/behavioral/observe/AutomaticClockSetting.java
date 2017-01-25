package com.wolf.behavioral.observe;

import java.util.Calendar;

/**
 * <b>功能</b>subject只关心observer接口，具体什么不关心。
 *
 * @author 李超
 * @Date 2016/7/15
 */
public class AutomaticClockSetting extends AbstractClockSetting {

	@Override
	public void setDate(){
		Calendar calendar = Calendar.getInstance();
		this.currentDate = calendar.getTime();
	}

}

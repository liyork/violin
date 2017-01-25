package com.wolf.utils;

import java.util.concurrent.TimeUnit;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/22
 * Time: 9:07
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class DateJudgeUtils {

	public static boolean ifExceedTime(long firstTime,long secondTime,long duration,TimeUnit timeUnit){

		long oneMinuteLater = firstTime + timeUnit.toMillis(duration);
		//超过1分钟主动关闭。
		return oneMinuteLater < secondTime;
	}
}

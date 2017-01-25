package com.wolf.test.base;

import com.wolf.utils.DateUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * <p> Description:
 * <p/>
 * Date: 2015/9/21
 * Time: 10:19
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class DateUtilsTest {

	@Test
	public void testSetFirstDayOfWeek() {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.YEAR, 2014);
		now.set(Calendar.MONTH, 9);
		now.set(Calendar.DAY_OF_MONTH, 19);
		//一周第一天是否为星期天(默认礼拜日是第一天)
		boolean isFirstDayIsSunday = (now.getFirstDayOfWeek() == Calendar.SUNDAY);
		System.out.println("isFirstDayIsSunday==" + isFirstDayIsSunday);
		//获取周几(从周日开始算)
		int weekDay = now.get(Calendar.DAY_OF_WEEK);
		System.out.println(weekDay);

		//这句对结果没有影响，只是测试下是否只对Calendar.WEEK_OF_MONTH和Calendar.WEEK_OF_YEAR起作用
		now.setFirstDayOfWeek(Calendar.MONDAY);
		//setFirstDayOfWeek只对Calendar.WEEK_OF_MONTH和Calendar.WEEK_OF_YEAR起作用
		int weekDay1 = now.get(Calendar.DAY_OF_WEEK);
		System.out.println(weekDay1);
		//转换成第一天为monday
		if (isFirstDayIsSunday) {
			//若一周第一天为星期天，减去1后等于0
			weekDay = weekDay - 1;
			if (weekDay == 0) {
				weekDay = 7;
			}
		}
		//打印周几
		//若当天为2014年10月13日（星期一），则打印输出：1
		//若当天为2014年10月17日（星期五），则打印输出：5
		//若当天为2014年10月19日（星期日），则打印输出：7
		System.out.println(weekDay);

		//月应有天数，31
		int days = now.getMaximum(Calendar.DAY_OF_MONTH);
		System.out.println(days);
		//月实际天数
		int days1 = now.getActualMaximum(Calendar.DAY_OF_MONTH);
		System.out.println(days1);
	}

	//月是从0-11
	// set:设定负值表示从当年1月倒退几个月，设定大于11的值表示从当前12月前进(12-value)个月
	@Test
	public void testSetAndAdd() {
		//====set month
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 0);
		System.out.println(calendar.getTime());

		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.MONTH, -1);//去年12月
		System.out.println(calendar1.getTime());

		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(Calendar.MONTH, 12);//明年1月
		System.out.println(calendar2.getTime());
		//====set day
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.MONTH, 2);
		instance.set(Calendar.DAY_OF_MONTH, 0);//本月减1天，
//		instance.set(Calendar.DAY_OF_MONTH,-1);//本月减2天，
		System.out.println(instance.getTime());
		//====add
		Calendar calendar3 = Calendar.getInstance();
		calendar3.set(Calendar.MONTH, 0);
		calendar3.add(Calendar.MONTH, -1);//去年12月，年也变化
		System.out.println(calendar3.getTime());
	}

	@Test
	public void testGetLastDayOfPreMonth() {

		for (int i = 0; i < 12; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, i);
			DateUtils.getLastDayOfPreMonth(calendar);
		}
	}

	//测试DAY_OF_WEEK 含有几个数
	@Test
	public void testDayOfWeekHasType() {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.YEAR, 2015);
		instance.set(Calendar.MONTH, 11);

		//20 - 26 为1,2,3,4,5,6,7  ,  27为1
		for (int i = 20; i <= 27; i++) {
			instance.set(Calendar.DAY_OF_MONTH, i);
			System.out.println("day:" + i);
			int i1 = instance.get(Calendar.DAY_OF_WEEK);
			System.out.println("DAY_OF_WEEK:" + i1);
			int i2 = instance.get(Calendar.DAY_OF_MONTH);
			System.out.println("DAY_OF_MONTH:" + i2);
			System.out.println();
		}
	}

	/**
	 * 最好不要设定setFirstDayOfWeek为monday(目前还没看明白)
	 * 测试DAY_OF_WEEK
	 * 从周日到周六
	 * 20 21 22 23 24 25 26 27
	 * 日 一  二 三 四  五 六 日
	 * 1  2  3  4  5  6  7  1
	 */
	@Test
	public void testDayOfWeek() {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.YEAR, 2015);
		instance.set(Calendar.MONTH, 11);
		instance.set(Calendar.DAY_OF_MONTH, 24);
		//todo 注意：此处需要先get下，不然下面的日期不对。。。
		System.out.println(instance.getTime());

		//默认是SUNDAY = 1
		int firstDayOfWeek = instance.getFirstDayOfWeek();
		System.out.println("first:" + firstDayOfWeek);
		//默认SUNDAY为一个礼拜第一天，那么整个礼拜就是：20--26
		instance.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);//25
		System.out.println(instance.getTime());
		instance.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);//26
		System.out.println(instance.getTime());
		instance.set(Calendar.DAY_OF_WEEK, 8);//20
		System.out.println(instance.getTime());
		instance.set(Calendar.DAY_OF_WEEK, 9);//21
		System.out.println(instance.getTime());

		System.out.println();

		Calendar instance1 = Calendar.getInstance();
		instance1.set(Calendar.YEAR, 2015);
		instance1.set(Calendar.MONTH, 11);
		instance1.set(Calendar.DAY_OF_MONTH, 24);
		//todo 注意：此处set触发时间调整，要不还得get一下才生效
		//设定MONDAY为一个礼拜第一天，那么整个礼拜就是：21--27
		instance1.setFirstDayOfWeek(Calendar.MONDAY);
		int firstDayOfWeek1 = instance1.getFirstDayOfWeek();
		System.out.println("first:" + firstDayOfWeek1);
		instance1.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);//25
		System.out.println(instance1.getTime());
		instance1.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);//26
		System.out.println(instance1.getTime());
		instance1.set(Calendar.DAY_OF_WEEK, 8);//20
		System.out.println(instance1.getTime());
		instance1.set(Calendar.DAY_OF_WEEK, 9);//21
		System.out.println(instance1.getTime());
	}


	/**
	 * 测试DAY_OF_MONTH
	 * 12月，
	 * 如果默认一周从周日开始则第一周是1-5
	 * 如果设定一周从周一开始则第一周是1-6
	 *
	 * @return
	 */
	@Test
	public void testWeekOfMonth() {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.YEAR, 2015);
		instance.set(Calendar.MONTH, 11);
		instance.set(Calendar.DAY_OF_MONTH, 6);

		int i = instance.get(Calendar.WEEK_OF_MONTH);
		System.out.println(i);

		instance.setFirstDayOfWeek(Calendar.MONDAY);

		int i1 = instance.get(Calendar.WEEK_OF_MONTH);
		System.out.println(i1);

	}

	@Test
	public void testGetCurrentDay() {
		Date currentDay = DateUtils.getCurrentDay();
		System.out.println(currentDay);
	}

	@Test
	public void testGetNextDay() {
		Date nextDay = DateUtils.getNextDay();
		System.out.println(nextDay);
	}

	@Test
	public void testGetMondayOfThisWeek() {
		Date mondayOfThisWeek = DateUtils.getMondayOfThisWeek();
		System.out.println(mondayOfThisWeek);
	}

	@Test
	public void testGetMondayOfPreWeek() {
		Date mondayOfPreWeek = DateUtils.getMondayOfPreWeek();
		System.out.println(mondayOfPreWeek);
	}

	@Test
	public void testGetFirstDayOfThisMonth() {
		Date firstDayOfThisMonth = DateUtils.getFirstDayOfThisMonth();
		System.out.println(firstDayOfThisMonth);
	}

	@Test
	public void testGetFirstDayOfPreMonth() {
		Date firstDayOfPreMonth = DateUtils.getFirstDayOfPreMonth();
		System.out.println(firstDayOfPreMonth);
	}

	@Test
	public void testConvertTime2Day() {
		String s = DateUtils.convertTime2Day(604800);
		System.out.println(s);
	}

	@Test
	public void testGetWeekScope() {
		String weekScope = DateUtils.getWeekScope(2016, 3, 1);
		System.out.println(weekScope);
	}

	@Test
	public void testGetCertainMonthOfLastDay() {
		Date certainMonthOfLastDay = DateUtils.getCertainMonthOfLastDay(9);
		System.out.println(certainMonthOfLastDay);
	}

	@Test
	public void testChangeDate() {
		Date date = DateUtils.changeDate(3);
		System.out.println(date);
	}

	@Test
	public void testChangeMonth() {
		Date date = DateUtils.changeMonth(3);
		System.out.println(date);
	}

	@Test
	public void testGetBeginEndDate() {
		Date[] beginEndDate = DateUtils.getBeginEndDate("2016-9-13");
		System.out.println(beginEndDate[0]);
		System.out.println(beginEndDate[1]);

		Date[] beginEndDate1 = DateUtils.getBeginEndDate("2016-9");
		System.out.println(beginEndDate1[0]);
		System.out.println(beginEndDate1[1]);
	}

	@Test
	public void testGetQueryDateRange() {
		for (int i = 1; i < 6; i++) {
			Date[] beginEndDate = DateUtils.getQueryDateRange(i);
			System.out.println(beginEndDate[0]);
			System.out.println(beginEndDate[1]);
		}
	}

	@Test
	public void testGetDifferMin() {
		Calendar instance = Calendar.getInstance();
		Calendar instance2 = Calendar.getInstance();
		instance2.add(Calendar.DAY_OF_MONTH,1);
		long differMin = DateUtils.getDifferMin(instance.getTime(), instance2.getTime());
		System.out.println(differMin);
	}
}

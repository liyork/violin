package com.wolf.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p> Description:
 * <p/>
 * Date: 2015/9/8
 * Time: 11:08
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class DateUtils {

	/**
	 * 将毫秒转换成天时分秒
	 */
	public static String convertTime2Day(long time) {

		long days = time / (1000 * 60 * 60 * 24);
		long hours = (time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (time % (1000 * 60)) / 1000;
		return days + " days " + hours + " hours " + minutes + " minutes "
				+ seconds + " seconds ";
	}

	/**
	 * Description: 获取今天
	 * <p>
	 * <pre>
	 * 例如，今天是2015-07-20（星期一），则返回2015-07-21 00:00:00
	 * <pre>
	 */
	public static Date getCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		return setCalendarVal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	}

	/**
	 * Description: 获取明天
	 * <p>
	 * <pre>
	 * 例如，今天是2015-07-20（星期一），则返回2015-07-21 00:00:00
	 * <pre>
	 */
	public static Date getNextDay() {
		Calendar calendar = Calendar.getInstance();
		return setCalendarVal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
	}


	/**
	 * Description: 获取本周的周一
	 * <p>
	 * <pre>
	 * 例如，今天是2015-07-20（星期一），则返回2015-07-20 00:00:00
	 * <pre>
	 */
	public static Date getMondayOfThisWeek() {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		return setCalendarVal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	}


	/**
	 * Description: 获取上周的周一
	 * <p>
	 * <pre>
	 * 例如，今天是2015-07-20（星期一），则返回2015-07-13 00:00:00
	 * <pre>
	 */
	public static Date getMondayOfPreWeek() {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		return setCalendarVal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH) - 7, 0, 0, 0);
	}


	/**
	 * Description: 获取本月的第一天
	 * <p>
	 * <pre>
	 * 例如，今天是2015-07-20（星期一），则返回2015-07-01 00:00:00
	 * <pre>
	 */
	public static Date getFirstDayOfThisMonth() {
		Calendar calendar = Calendar.getInstance();
		return setCalendarVal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
	}

	/**
	 * Description: 获取上月的第一天
	 * <p>
	 * <pre>
	 * 例如，今天是2015-07-20（星期一），则返回2015-06-01 00:00:00
	 * <pre>
	 */
	public static Date getFirstDayOfPreMonth() {
		Calendar calendar = Calendar.getInstance();
		return setCalendarVal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 1, 1, 0, 0, 0);
	}

	public static Date setCalendarVal(int year, int month, int day, int hour, int minute, int second) {
		return initCalendarInstance(year, month, day, hour, minute, second).getTime();
	}

	public static Calendar initCalendarInstance(int year, int month, int day, int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}


	public static Date getLastDayOfPreMonth(Calendar cal) {

		int month = cal.get(Calendar.MONTH);
		int year;
		//当前月是1月下标为0，-1后跨年了
		if (month == 0) {
			year = cal.get(Calendar.YEAR) - 1;
			month = 11;
		} else {
			year = cal.get(Calendar.YEAR);
			month = month - 1;
		}
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		//实际月最大天数
		int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, day);

		return cal.getTime();
	}


	/**
	 * 获取周范围
	 * 根据年月来判断该月第n周的周一至周日
	 * 比如 2016年3月第1周的周一是 2月29日 则传year=2016 month=3 week=1
	 * 返回字符串  02-29至03-06
	 *
	 * @param year
	 * @param month
	 * @param week
	 * @return
	 */
	public static String getWeekScope(int year, int month, int week) {
		Calendar calendar = initCalendarInstance(year, month - 1, 1, 0, 0, 0);

		Date beginDate = setCalendarVal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				7 * (week - 1), 0, 0, 0);

		Date endDate = setCalendarVal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				7 * (week - 1) + 6, 0, 0, 0);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");

		String beginDateStr = simpleDateFormat.format(beginDate);
		String endDateStr = simpleDateFormat.format(endDate);

		return beginDateStr + "至" + endDateStr;

	}

	/**
	 * Description: 获取某月的最后一天
	 */
	public static Date getCertainMonthOfLastDay(int month) {
		Calendar calendar = Calendar.getInstance();
		//设置年份
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		//设置月份
		calendar.set(Calendar.MONTH, month - 1);
		//获取某月最大天数
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		return setCalendarVal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), lastDay, 0, 0, 0);
	}

	/**
	 * Description: 加减日期
	 */
	public static Date changeDate(int dayNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, dayNum);
		return setCalendarVal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	}

	/**
	 * Description: 加减月
	 */
	public static Date changeMonth(int monthNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, monthNum);
		return setCalendarVal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	}


	/**
	 * 根据字符串获得开始和结束日期
	 *
	 * @param queryDate 不可为空，yyyy-MM-dd或者yyyy-MM
	 * @return date[0]:开始日期
	 * date[1]:结束日期
	 */
	public static Date[] getBeginEndDate(String queryDate) {
		Date[] dates = new Date[2];
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(queryDate)) {
			String[] splitDate = queryDate.split("-");
			int year = Integer.parseInt(splitDate[0]);
			int month = Integer.parseInt(splitDate[1]) - 1;
			if (splitDate.length == 2) {//查询月
				Date beginDate = DateUtils.setCalendarVal(year, month, 1, 0, 0, 0);
				dates[0] = beginDate;
				Date endDate = DateUtils.setCalendarVal(year, month + 1, 1, 0, 0, 0);
				dates[1] = endDate;
			} else if (splitDate.length == 3) {//查询天
				int day = Integer.parseInt(splitDate[2]);
				Date beginDate = DateUtils.setCalendarVal(year, month, day, 0, 0, 0);
				dates[0] = beginDate;
				Date endDate = DateUtils.setCalendarVal(year, month, day + 1, 0, 0, 0);
				dates[1] = endDate;
			} else {
				throw new RuntimeException("参数queryDate不符合要求,queryDate:" + queryDate);
			}
		}
		return dates;
	}


	/**
	 * 根据规定范围查询指定日期
	 *
	 * @param queryDateRange 不可为空，1,2,3,4,5
	 * @return date[0]:开始日期
	 * date[1]:结束日期
	 */
	public static Date[] getQueryDateRange(int queryDateRange) {
		Date[] dates = new Date[2];
		switch (queryDateRange) {
			case 1://今日
				dates[0] = getCurrentDay();
				dates[1] = getNextDay();
				break;
			case 2://本周
				dates[0] = getMondayOfThisWeek();
				dates[1] = getNextDay();
				break;
			case 3://上周
				dates[0] = getMondayOfPreWeek();
				dates[1] = getMondayOfThisWeek();
				break;
			case 4://本月
				dates[0] = getFirstDayOfThisMonth();
				dates[1] = getNextDay();
				break;
			case 5://上月
				dates[0] = getFirstDayOfPreMonth();
				dates[1] = getFirstDayOfThisMonth();
				break;
			default:
				break;
		}
		return dates;
	}

	/**
	 * 相差分钟数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDifferMin(Date date1, Date date2) {
		return (date2.getTime() - date1.getTime()) / (1000 * 60);
	}

	public static long getDifferHour(Date date1, Date date2) {
		return (date2.getTime() - date1.getTime()) / (1000 * 60 * 60);
	}

	public static int getDifferDay(Date date1, Date date2) {
		return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
	}

	public static int getDifferMonth(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		return calendar2.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH);
	}


	public static String getTimeDescByNow(Date date) {

		Date currentDate = new Date();
		if (date.getTime() > currentDate.getTime()) {
			throw new RuntimeException("申请日期不能大于当前时间");
		}

		String applyTimeDesc;
		long differMin = getDifferMin(date, currentDate);
		if (differMin == 0) {
			applyTimeDesc = "刚刚";
		} else if (differMin >= 1 && differMin <= 59) {
			applyTimeDesc = differMin + "分钟前";
		} else {
			long differHour = getDifferHour(date, currentDate);
			if (differHour >= 1 && differHour < 24) {
				applyTimeDesc = differHour + "小时前";
			} else {
				long differDay = getDifferDay(date, currentDate);
				if (differDay >= 1 && differDay <= 30) {
					applyTimeDesc = differDay + "天前";
				} else {
					int differMonth = getDifferMonth(date, currentDate);
					if (differMonth >= 1 && differMonth <= 6) {
						applyTimeDesc = differMonth + "个月前";
					} else {
						throw new RuntimeException("申请日期超过6个月");
					}
				}
			}
		}
		return applyTimeDesc;
	}
}

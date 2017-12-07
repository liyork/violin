package com.wolf.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> Description:日期安全工具类使用map存放pattern对应的threadlocal，被每个线程用
 * 			每个pattern对应一个threadLocal，一个threadloca对应到多个thread里，懒加载
 * <p/>
 * Date: 2016/3/29
 * Time: 10:39
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ThreadSafeDateUtils2 {

	//日期解析格式统一在此添加
	public static final String PATTERN_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_YYYYMMDD_HHMM = "yyyy-MM-dd HH:mm";
	public static final String PATTERN_YYYYMMDD = "yyyy-MM-dd";

	//中文日期格式
	public static final String PATTERN_ZHCN_YYYYMMDD_HHMMSS = "yyyy年MM月dd日 HH时mm分ss秒";
	public static final String PATTERN_ZHCN_YYYYMMDD_HHMM = "yyyy年MM月dd日 HH时mm分";
	public static final String PATTERN_ZHCN_YYYYMMDD = "yyyy年MM月dd日";

	public static final String PATTERN_ZHCN_MMDD_HHMM = "MM月dd日 HH:mm";

	/** 锁对象 */
	private static final Object lockObj = new Object();

	/** 存放不同的日期模板格式的sdf的Map */
	private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	private ThreadSafeDateUtils2(){}

	private static SimpleDateFormat getInstance(final String pattern) {
		ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

		// 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
		//todo 双重检查会导致使用了未初始化完全的对象，有待改进
		if (tl == null) {
			synchronized (lockObj) {
				tl = sdfMap.get(pattern);
				if (tl == null) {
					// 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
					tl = new ThreadLocal<SimpleDateFormat>() {
						@Override
						protected SimpleDateFormat initialValue() {
							return new SimpleDateFormat(pattern);
						}
					};
					sdfMap.put(pattern, tl);
				}
			}
		}
		return tl.get();
	}

	/**
	 * 根据pattern格式将Date转成String
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		return getInstance(pattern).format(date);
	}

	/**
	 * 根据pattern格式将String转成Date
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date parse(String dateStr, String pattern) {
		Date result = null;
		try {
			result = getInstance(pattern).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

    public static void main(String args[]){
        //parse 方法测试
        System.out.println(parse("2016-06-24 13:00:00", PATTERN_YYYYMMDD_HHMMSS));
        System.out.println(parse("2016-06-24 13:00:00", PATTERN_YYYYMMDD_HHMM));
        System.out.println(parse("2016-06-24 13:00:00", PATTERN_YYYYMMDD));

        //format方法测试
        System.out.println(format(new Date(), PATTERN_YYYYMMDD_HHMMSS));
        System.out.println(format(new Date(), PATTERN_YYYYMMDD_HHMM));
        System.out.println(format(new Date(), PATTERN_YYYYMMDD));

    }

}

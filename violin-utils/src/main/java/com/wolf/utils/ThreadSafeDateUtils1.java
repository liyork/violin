package com.wolf.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p> Description:日期安全工具类
 * <p/>
 * Date: 2016/3/29
 * Time: 10:39
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ThreadSafeDateUtils1 {

	//会引发多线程问题
//	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static ThreadLocal<SimpleDateFormat> threadLocal_yyyy_MM_dd = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			//对于每个线程使用自己的SimpleDateFormat
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	private static ThreadLocal<SimpleDateFormat> threadLocal_yyyy_MM_dd_HH_mm = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			//对于每个线程使用自己的SimpleDateFormat
			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
	};
	private static ThreadLocal<SimpleDateFormat> threadLocal_yyyy_MM_dd_HH_mm_ss = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			//对于每个线程使用自己的SimpleDateFormat
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	//可用来以后扩展的格式
	public static final String PATTERN_ZHCN_YYYYMMDD_HHMMSS = "yyyy年MM月dd日 HH时mm分ss秒";
	public static final String PATTERN_ZHCN_YYYYMMDD_HHMM = "yyyy年MM月dd日 HH时mm分";
	public static final String PATTERN_ZHCN_YYYYMMDD = "yyyy年MM月dd日";
	public static final String PATTERN_ZHCN_MMDD_HHMM = "MM月dd日 HH:mm";

	//此处不能用这种方式，因为从threadLocal取出后，applyPattern时会产生多线程问题。
//	public Date convert2Date(String date,String pattern){
//		Date result = null;
//		try {
//			SimpleDateFormat simpleDateFormat = threadLocal_yyyy_MM_dd.get();
//			simpleDateFormat.applyPattern(pattern);
//			simpleDateFormat.parse(date);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}

	public static Date convert2Date_yyyy_MM_dd(String date){
		Date result = null;
		try {
			result = threadLocal_yyyy_MM_dd.get().parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Date convert2Date_yyyy_MM_dd_HH_mm(String date){
		Date result = null;
		try {
			result = threadLocal_yyyy_MM_dd_HH_mm.get().parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Date convert2Date_yyyy_MM_dd_HH_mm_ss(String date){
		Date result = null;
		try {
			result = threadLocal_yyyy_MM_dd_HH_mm_ss.get().parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}


	public static String convert2String_yyyy_MM_dd(Date date){
		return threadLocal_yyyy_MM_dd.get().format(date);
	}
	public static String convert2String_yyyy_MM_dd_HH_mm(Date date){
		return threadLocal_yyyy_MM_dd_HH_mm.get().format(date);
	}
	public static String convert2String_yyyy_MM_dd_HH_mm_ss(Date date){
		return threadLocal_yyyy_MM_dd_HH_mm_ss.get().format(date);
	}


	//每次写完功能一定要测试，如果没有这个测试，当时方法忘了赋值给result了。。。threadLocal_yyyy_MM_dd.get().parse(date);
	public static void main(String[] args) {
		Date date = convert2Date_yyyy_MM_dd("2016-06-17");
		System.out.println(date);

		String s = convert2String_yyyy_MM_dd(date);
		System.out.println(s);
		String s1 = convert2String_yyyy_MM_dd_HH_mm(date);
		System.out.println(s1);
		String s2 = convert2String_yyyy_MM_dd_HH_mm_ss(new Date());
		System.out.println(s2);
	}

}

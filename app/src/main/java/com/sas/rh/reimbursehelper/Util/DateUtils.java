package com.sas.rh.reimbursehelper.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期处理工具类
 * @author tuzhengsong
 */
public class DateUtils {

	/** 定义常量 **/
	public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_LONG_STR = "yyyy-MM-dd kk:mm:ss.SSS";
	public static final String DATE_SMALL_STR = "yyyy-MM-dd";
	public static final String DATE_KEY_STR = "yyMMddHHmmss";
	public static final String DATE_All_KEY_STR = "yyyyMMddHHmmss";

	/**
	 * 给指定的日期加上(减去)月份
	 * @param date
	 * @param pattern
	 * @param num
	 * @return
	 */
	public static String addMoth(Date date, String pattern, int num) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.MONTH, num);
		return simpleDateFormat.format(calender.getTime());
	}

	/**
	 * 给制定的时间加上(减去)天
	 * @param date
	 * @param pattern
	 * @param num
	 * @return
	 */
	public static String addDay(Date date, String pattern, int num) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.DATE, num);
		return simpleDateFormat.format(calender.getTime());
	}

	/**
	 * 获取系统当前时间
	 * @return
	 */
	public static String getNowTime() {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
		return df.format(new Date());
	}

	/**
	 * 获取系统当前时间(指定返回类型)
	 * @return
	 */
	public static String getNowTime(String type) {
		SimpleDateFormat df = new SimpleDateFormat(type);
		return df.format(new Date());
	}

	public static String getDiff(long start, long end) {
		long between = end - start;
		long hour = (between / (60 * 60 * 1000));
		long min = ((between / (60 * 1000)) - hour * 60);
		long s = (between / 1000 - hour * 60 * 60 - min * 60);
		long ms = (between - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return (hour + "小时" + min + "分" + s + "秒" + ms + "毫秒");
	}

	/**
	 * 使用预设格式提取字符串日期
	 * @param date 日期字符串
	 * @return
	 */
	public static Date parse(String date) {
		return parse(date, DATE_FULL_STR);
	}

	/**
	 * 指定指定日期字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date parse(String date, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 两个时间比较
	 * @param
	 * @return
	 */
	public static int compareDateWithNow(Date date) {
		Date now = new Date();
		int rnum = date.compareTo(now);
		return rnum;
	}

	/**
	 * 两个时间比较(时间戳比较)
	 * @param
	 * @return
	 */
	public static int compareDateWithNow(long date) {
		long now = dateToUnixTimestamp();
		if (date > now) {
			return 1;
		} else if (date < now) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * 将指定的日期转换成Unix时间戳
	 * @param date 需要转换的日期 yyyy-MM-dd HH:mm:ss
	 * @return long 时间戳
	 */
	public static long dateToUnixTimestamp(String date) {
		long timestamp = 0;
		try {
			timestamp = new SimpleDateFormat(DATE_FULL_STR).parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return timestamp;
	}

	/**
	 * 将指定的日期转换成Unix时间戳
	 * @param date 需要转换的日期 yyyy-MM-dd
	 * @return long 时间戳
	 */
	public static long dateToUnixTimestamp(String date, String dateFormat) {
		long timestamp = 0;
		try {
			timestamp = new SimpleDateFormat(dateFormat).parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}

	/**
	 * 将当前日期转换成Unix时间戳
	 * @return long 时间戳
	 */
	public static long dateToUnixTimestamp() {
		long timestamp = System.currentTimeMillis();
		return timestamp;
	}

	/**
	 * 将Unix时间戳转换成日期
	 * @param timestamp 时间戳
	 * @return String 日期字符串
	 */
	public static String unixTimestampToDate(long timestamp) {
		SimpleDateFormat sd = new SimpleDateFormat(DATE_FULL_STR);
		sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sd.format(new Date(timestamp));
	}

	/**
	 * 将Unix时间戳转换成日期
	 * @param timestamp 时间戳
	 * @return String 日期字符串
	 */
	public static String TimeStamp2Date(long timestamp, String dateFormat) {
		String date = new SimpleDateFormat(dateFormat).format(new Date(timestamp));
		return date;
	}

	/**
	 * 将Unix时间戳转换成日期
	 * @param timestamp 时间戳
	 * @return String 日期字符串
	 */
	public static String TimeStamp2Date(long timestamp) {
		String date = new SimpleDateFormat(DATE_FULL_STR).format(new Date(timestamp));
		return date;
	}

    public static String dateToString(Date date, String yyyyMMdd) {
		DateFormat df = new SimpleDateFormat(yyyyMMdd);
		return df.format(date);
	}

	public static String parse(Date date) {
		String dt = date.toString();

		SimpleDateFormat sdf1= new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

		SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = null;
		try {
			 result = sdf2.format(sdf1.parse(dt));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
}

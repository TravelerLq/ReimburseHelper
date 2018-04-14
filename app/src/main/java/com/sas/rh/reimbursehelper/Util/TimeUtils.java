/*
 * Copyright (c) 2017 李虎
 * Copyright (c) 2017 李世界
 * Copyright (c) 2017 朱璟
 * Copyright (c) 2017 heisenberg.gong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sas.rh.reimbursehelper.Util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeUtils {
	
	public static String DEFAULT_FORMAT = "yyyyMMddHHmmss";
	public static String DEFAULT_FORMAT_2 = "yyyy-MM-dd HH:mm:ss";
	public static String DEFAULT_FORMAT_3 = "yyyy/MM/dd HH:mm:ss";
	public static String DEFAULT_FORMAT_4 = "MM/dd HH:mm:ss";
	public static String DEFAULT_FORMAT_5= "MM-dd HH:mm:ss";
	public static String DEFAULT_FORMAT_6= "HH:mm";
	public static String DEFAULT_FORMAT_7= "MM-dd";
	public static String DEFULT_FORMATE_8 = "yyyy-MM-dd";
	public static long getTodayZero() {
		Date date = new Date();
		long l = 24 * 60 * 60 * 1000; // 每天的毫秒数
		// date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（
		// 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
		// 减8个小时的毫秒值是为了解决时区的问题。
		return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
	}

	public static String getZeroTime() {
		long time = getTodayZero();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date(time);
		return format.format(d1);
	}

	public static String getTweTime() {
		long time = getTodayZero() + 24 * 60 * 60 * 1000 - 1;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date(time);
		return format.format(d1);
	}

	public static String getZeroTimeByCalander() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		// 打印时间戳
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date(calendar.getTimeInMillis());
		return format.format(d1);
	}

	public static String getTweTimeByCalander() {
		Calendar currentDate = Calendar.getInstance();

		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		// 打印时间戳
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date(currentDate.getTimeInMillis());
		return format.format(d1);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getUploadDateTimeString(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(time);
		return formatter.format(curDate);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getUploadDateTimeString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date();
		return formatter.format(curDate);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCurDateString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date();
		return formatter.format(curDate);
	}

	@SuppressLint("SimpleDateFormat")
	public static long getDifftimeValue(String afterTime, String beforeTime) {

		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date begin;
		try {
			begin = dfs.parse(afterTime);
			Date end = dfs.parse(beforeTime);
			long between = (begin.getTime() - end.getTime()) / 1000;// 除以1000是为了转换成秒
			Loger.i("====时间差===" + between + "," + beforeTime+"-"+afterTime);
			return between;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 将String时间格式字符串转换为 MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static String formartToMMDD(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("MM-dd");
		Date strtodate = null;
		try {
			strtodate = formatter.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatter2.format(strtodate);
	}
	/**
	 * 将String时间格式字符串转换为 dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static String formartToDD(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd");
		Date strtodate = null;
		try {
			strtodate = formatter.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatter2.format(strtodate);
	}

	public static String getTomorrowDate() {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}
	public static String getSpecifiedDate(int time) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, time);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getDefaultString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date();
		return formatter.format(curDate);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getDefault2String() {
		SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT_2);
		Date curDate = new Date();
		return formatter.format(curDate);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getYYMMDDString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Date curDate = new Date();
		return formatter.format(curDate);
	}


	public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
	
	public static Date longToDate(long currentTime, String formatType)
			 throws ParseException {
			 Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
			 String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
			 Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
			 return date;
	}
	
	public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }
	
	public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }
	
	 // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }
    
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static String stringToString(String strTime, String formatType, String afterType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        return dateToString(date, afterType);
    }
    
    
    public static long dateToLong(Date date) {
        return date.getTime();
    }
    
    /**
	 * 将String时间格式字符串转换为 MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static String formartToYYYYMMDD(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		Date strtodate = null;
		try {
			strtodate = formatter.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatter2.format(strtodate);
	}
	
	/**
	 * 时间比较
	 * @param start
	 * @param end
	 * @param formart 时间格式
	 * @return true：start在前；FALSE：start在后
	 */
	public static boolean timeCompare(String start, String end, String formart){
		SimpleDateFormat sdf = new SimpleDateFormat(formart, Locale.CHINA);
		Date startDate = null;
		Date endDate = null;
		try {
			endDate = sdf.parse(end);
			startDate = sdf.parse(start);
			boolean flag = startDate.before(endDate);
            return flag;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean isSameDate(String strdate1, String strdate2, String format) {
		Date date1;
		Date date2;
		try {
			date2 = stringToDate(strdate2, format);
			date1 = stringToDate(strdate2, format);
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
		               .get(Calendar.YEAR);
		    boolean isSameMonth = isSameYear
		               && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		    boolean isSameDate = isSameMonth
		               && cal1.get(Calendar.DAY_OF_MONTH) == cal2
		                       .get(Calendar.DAY_OF_MONTH);

		    return isSameDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressLint("SimpleDateFormat")
	public static String geteTimeyyyyMMDDString() {
		SimpleDateFormat formatter = new SimpleDateFormat(DEFULT_FORMATE_8);
		Date curDate = new Date();
		return formatter.format(curDate);
	}

	public static String getTomorrowTime(){
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天

		Date tomorrow = c.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(tomorrow);
	}
}

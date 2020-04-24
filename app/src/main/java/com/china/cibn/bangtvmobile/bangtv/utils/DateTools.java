package com.china.cibn.bangtvmobile.bangtv.utils;

import android.content.Context;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTools {

	/*
	 * 将时间戳转为字符串 ，格式：yyyy-MM-dd HH:mm
	 */
	public static String getStrTime_ymd_hm(String cc_time) {
		String re_StrTime = "";
		if (TextUtils.isEmpty(cc_time) || "null".equals(cc_time)) {
			return re_StrTime;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;

	}

	/*
	 * 将时间戳转为字符串 ，格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String getStrTime_ymd_hms(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;

	}

	/*
	 * 将时间戳转为字符串 ，格式：yyyy.MM.dd
	 */
	public static String getStrTime_ymd(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：yyyy
	 */
	public static String getStrTime_y(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：MM-dd
	 */
	public static String getStrTime_md(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：HH:mm
	 */
	public static String getStrTime_hm(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：HH:mm:ss
	 */
	public static String getStrTime_hms(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：MM-dd HH:mm:ss
	 */
	public static String getNewsDetailsDate(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将字符串转为时间戳
	 */
	public static String getTime() {
		String re_time = null;
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date d;
		d = new Date(currentTime);
		long l = d.getTime();
		String str = String.valueOf(l);
		re_time = str.substring(0, 10);
		return re_time;
	}

	/*
	 * 将字符串转为日期
	 */
	public static String getTimeToday(long times) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date d;
		d = new Date(times);
		String str = sdf.format(d);
		re_time = str.substring(5, 10);
		return re_time;
	}

	/*
	 * 将时间戳转为字符串 ，格式：yyyy.MM.dd 星期几
	 */
	public static String getSection(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  EEEE");
		// 对于创建SimpleDateFormat传入的参数：EEEE代表星期，如“星期四”；MMMM代表中文月份，如“十一月”；MM代表月份，如“11”；
		// yyyy代表年份，如“2010”；dd代表天，如“25”
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/** 将时间String转换成long 例如2015-12-12 12:15 */
	public static long getLongTim(String strTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			return sdf.parse(strTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 计算两个##:##格式的时间 段(视频总播放时间--格式##:##String)
	 * 
	 * @param start
	 *            开始的时间
	 * @param end
	 *            结束的时间
	 * @return
	 */
	public static String getTimePeriod(String start, String end) {
		String timeStr = null;
		int startInt = Integer.parseInt(start.split(":")[0]) * 60
				+ Integer.parseInt(start.split(":")[1]);
		int endInt = Integer.parseInt(end.split(":")[0]) * 60
				+ Integer.parseInt(end.split(":")[1]);
		int timeInt = endInt - startInt;
		if (timeInt >= 60) {
			if (timeInt % 60 >= 10) {
				timeStr = timeInt / 60 + ":" + timeInt % 60 + ":00";
			} else {
				timeStr = timeInt / 60 + ":0" + timeInt % 60 + ":00";
			}
		} else {
			timeStr = timeInt + ":00";
		}
		return timeStr;
	}

	/**
	 * 计算两个##:##格式的时间 段(总时间int)---视频进度条使用
	 * 
	 * @param start
	 *            开始的时间
	 * @param end
	 *            结束的时间
	 * @return
	 */
	public static int getTimePeriodInt(String start, String end) {
		int timeInt = 0;
		int startInt = Integer.parseInt(start.split(":")[0]) * 60
				+ Integer.parseInt(start.split(":")[1]);
		int endInt = Integer.parseInt(end.split(":")[0]) * 60
				+ Integer.parseInt(end.split(":")[1]);
		timeInt = (endInt - startInt) * 60;

		return timeInt;
	}

	/**
	 * 将int型的时间转成##:##格式的时间
	 * 
	 * @return
	 */
	public static String millisToString(long millis) {
		boolean negative = millis < 0;
		millis = java.lang.Math.abs(millis);
		millis /= 1000;
		int sec = (int) (millis % 60);
		millis /= 60;
		int min = (int) (millis);
		String time;
		DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		format.applyPattern("00");
		time = format.format(min) + ":" + format.format(sec);
		time = negative ? "-" : "" + time;
		return time;
		//return formatDate(new Date(millis), PLAY_TIME_STRING);

	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}


	/**
	 * HH:mm:ss
	 */
	public static String generateTime(long position) {

		int totalSeconds = (int) ((position / 1000.0) + 0.5);

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		if (hours > 0) {
			return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
					seconds);
		} else {
			return String.format(Locale.US, "%02d:%02d", minutes, seconds);
		}
	}
}

package cn.dtvalley.chilopod.core.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class DateUtil {
	
	
	
	/**
	 * 格式化日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if(date==null) return null;
		if (StringUtils.isBlank(pattern)) {
			pattern = "yyyy-MM-dd";
		}
		return new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * 格式化日期
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		if(date==null) return null;
		String pattern = "yyyy-MM-dd";
		return new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * 解析字符串为日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date parse(String date, String pattern) {
		if(date==null) return null;
		if (StringUtils.isBlank(pattern)) {
			pattern = "yyyy-MM-dd";
		}
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	

}

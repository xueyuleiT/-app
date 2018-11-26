package com.manager.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CurrentTime {
	public static String dayNum = "0";
	public static String getCurrentTime(String format){
		SimpleDateFormat dateForamt= new SimpleDateFormat(format);
		Date curDate = new Date(System.currentTimeMillis());
		String time = dateForamt.format(curDate);
		return time;
	}
	public static String getData(int beforeData){
		Date dNow = new Date();   //当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, beforeData);  //设置为前一天
		dBefore = calendar.getTime();   //得到前一天的时间

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
		String defaultStartDate = sdf.format(dBefore);    //格式化前一天
		return defaultStartDate;
	}
}

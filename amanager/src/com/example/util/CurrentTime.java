package com.example.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ������
 * ��õ�ǰʱ�乤����
 * ***/
public class CurrentTime {
	//��õ�ǰʱ��  format�� "yyyy-MM-dd hh:mm:ss"
	public static String getCurrentTime(String format){
		SimpleDateFormat dateForamt= new SimpleDateFormat(format);
		Date curDate = new Date(System.currentTimeMillis());
		String time = dateForamt.format(curDate);
		return time;
	}
	public static String getBeforeMonthTime(String format){
		SimpleDateFormat dateForamt= new SimpleDateFormat(format);
		Date curDate = new Date(System.currentTimeMillis() + (30*24*60*60*1000));
		String time = dateForamt.format(curDate);
		return time;
	}
}

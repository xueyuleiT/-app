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
		Date dNow = new Date();   //��ǰʱ��
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //�õ�����
		calendar.setTime(dNow);//�ѵ�ǰʱ�丳������
		calendar.add(Calendar.DAY_OF_MONTH, beforeData);  //����Ϊǰһ��
		dBefore = calendar.getTime();   //�õ�ǰһ���ʱ��

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //����ʱ���ʽ
		String defaultStartDate = sdf.format(dBefore);    //��ʽ��ǰһ��
		return defaultStartDate;
	}
}

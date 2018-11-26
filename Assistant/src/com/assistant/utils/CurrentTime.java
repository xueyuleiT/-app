package com.assistant.utils;

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
	
}
